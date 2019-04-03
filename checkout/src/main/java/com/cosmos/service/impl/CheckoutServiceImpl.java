package com.cosmos.service.impl;

import com.cosmos.checkout.dto.*;
import com.cosmos.checkout.enums.OrderStateEnum;
import com.cosmos.checkout.enums.PaymentMode;
import com.cosmos.checkout.enums.TransactionType;
import com.cosmos.entity.OrderPayment;
import com.cosmos.entity.Orders;
import com.cosmos.entity.UserCosmosCash;
import com.cosmos.exception.CheckoutException;
import com.cosmos.repository.OrderPaymentRepository;
import com.cosmos.repository.OrdersRepository;
import com.cosmos.service.ICosmosCashService;
import com.cosmos.service.IcheckoutService;
import com.cosmos.utils.CheckoutUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Checkout service.
 *
 * @author ambujmehra
 */
@Service
public class CheckoutServiceImpl implements IcheckoutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutServiceImpl.class);

    @Autowired
    private CheckoutUtils checkoutUtils;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderPaymentRepository orderPaymentRepository;

    @Autowired
    private PaytmPaymentsService paytmPaymentsService;

    @Autowired
    private OmsServiceImpl omsService;

    @Autowired
    private ICosmosCashService cosmosCashService;


    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public InitiateCheckoutResponse initiateCheckout(InitiateCheckoutRequest initiateCheckoutRequest) {
        Orders checkoutOrder = checkoutUtils.getOrdersFromCheckoutRequest(initiateCheckoutRequest);
        CosmosCashDto userCosmosCash = cosmosCashService.getUserCosmosCashBalance(initiateCheckoutRequest.getUserCode());

        if (checkoutOrder.getTotalOrderAmount().compareTo(userCosmosCash.getCosmosCash()) <= 0) {
            LOGGER.info("User :: {} has cosmos cash more thean total amount", initiateCheckoutRequest.getUserCode());
            checkoutOrder.setActualOrderAmount(BigDecimal.ZERO);
            checkoutOrder.setCosmosCash(checkoutOrder.getTotalOrderAmount());
        } else {
            LOGGER.info("User :: {} has to do balance payment as cash is less", initiateCheckoutRequest.getUserCode());
            checkoutOrder.setCosmosCash(userCosmosCash.getCosmosCash());
            checkoutOrder.setActualOrderAmount(checkoutOrder.getTotalOrderAmount().subtract(userCosmosCash.getCosmosCash()));
        }
        Orders orders = ordersRepository.save(checkoutOrder);
        LOGGER.info("order created in checkout Db for user :: {} with orderid :: {} and trnx id :: {}",
                initiateCheckoutRequest.getUserCode(), orders.getTransactionId(), orders.getTransactionId());
        return InitiateCheckoutResponse.builder()
                .orderDate(orders.getOrderDate().getTime())
                .orderStatus(orders.getOrderStatus())
                .totalOrderAmount(orders.getTotalOrderAmount())
                .actualOrderAmount(orders.getActualOrderAmount())
                .usableCosmosCash(orders.getCosmosCash())
                .totalCosmosCash(userCosmosCash.getCosmosCash())
                .transactionId(orders.getTransactionId())
                .paymentOptions(Arrays.stream(PaymentMode.values())
                        .map(InitiateCheckoutResponse.PaymentOption::getFromPaymentMode)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponseDto initiatePayment(InitiatePaymentRequestDto initiatePaymentRequestDto) {

        Orders orders = ordersRepository.findByTransactionId(initiatePaymentRequestDto.getTransactionId());
        Optional.ofNullable(orders).orElseThrow(() -> new CheckoutException("Order not found"));
        PaymentResponseDto.PaymentOptionData paymentOptionData = null;

        if (initiatePaymentRequestDto.getActualOrderAmount().compareTo(BigDecimal.ZERO) > 0) {
            PaymentMode paymentMode = PaymentMode.getPaymentModeById(initiatePaymentRequestDto.getPaymentModeId());
            LOGGER.info("need to collect payment from external source");
            switch (paymentMode) {
                case PAYTM:
                    paymentOptionData = paytmPaymentsService.getPaymentsOptionData(initiatePaymentRequestDto);
                    OrderPayment orderPayment = new OrderPayment();
                    orderPayment.setCompleted(false);
                    orderPayment.setOrder(orders);
                    orderPayment.setTotalOrderAmount(initiatePaymentRequestDto.getActualOrderAmount());// this is final discount paytm amount
                    orderPayment.setTransactionType(TransactionType.DEBIT);
                    orderPayment.setPaymentMode(PaymentMode.PAYTM);
                    orderPaymentRepository.save(orderPayment);
                    break;
                default:
            }
        }

        if (initiatePaymentRequestDto.getCosmosCashUsed().compareTo(BigDecimal.ZERO) > 0) {
            OrderPayment orderPayment = new OrderPayment();
            orderPayment.setCompleted(false);
            orderPayment.setOrder(orders);
            orderPayment.setTotalOrderAmount(initiatePaymentRequestDto.getActualOrderAmount());// this is final discount paytm amount
            orderPayment.setTransactionType(TransactionType.DEBIT);
            orderPayment.setPaymentMode(PaymentMode.COSMOS_CASH);
            orderPaymentRepository.save(orderPayment);
        }

        OmsRequest omsRequest = OmsRequest.builder()
                .orderStatus(OrderStateEnum.ORDER_PAYMENT_INITIATE.getOrderState())
                .orderUpdateMessage("Payment initiated with external payment mode"
                        + initiatePaymentRequestDto.getPaymentModeId())
                .transactionId(initiatePaymentRequestDto.getTransactionId())
                .userCode(initiatePaymentRequestDto.getUserCode())
                .build();
        omsService.updateOrderStatus(omsRequest);
        return PaymentResponseDto.builder()
                .paymentOptionData(paymentOptionData)
                .totalOrderAmount(initiatePaymentRequestDto.getTotalOrderAmount())
                .actualOrderAmount(initiatePaymentRequestDto.getActualOrderAmount())
                .cosmosCash(initiatePaymentRequestDto.getCosmosCashUsed())
                .transactionId(initiatePaymentRequestDto.getTransactionId())
                .build();
    }

    @Override
    public PaymentCallbackResponseDto initiateCallbackPayment(PaymentCallbackRequestDto paymentCallbackRequestDto) {
        PaymentMode paymentMode = PaymentMode.getPaymentModeById(paymentCallbackRequestDto.getPaymentModeId());
        String cosmosTransactionId;
        String paymentsModeTransactionId;
        PaymentCallbackResponseDto paymentCallbackResponseDto = null;
        switch (paymentMode) {
            case PAYTM:
                boolean paytmTransactionStatus;
                paymentCallbackRequestDto.getPaymentResponseParams().remove("ORDER_ID");
                cosmosTransactionId = paytmPaymentsService.getCosmosTransactionIdFromCallbackParams(
                        paymentCallbackRequestDto.getPaymentResponseParams());
                paymentsModeTransactionId = paytmPaymentsService.getPaymentModeTransactionId(
                        paymentCallbackRequestDto.getPaymentResponseParams());
                if (cosmosTransactionId != null && paymentsModeTransactionId != null) {
                    paytmPaymentsService.validateCallbackChecksum(paymentCallbackRequestDto.getPaymentResponseParams());
                    paytmTransactionStatus = paytmPaymentsService.checkOrderstatusFromPaymentMode(
                            paymentCallbackRequestDto.getPaymentResponseParams());
                } else {
                    throw new CheckoutException("Could not get cosmos and payment mode transaction ids");
                }
                OmsRequest omsRequest = OmsRequest.builder()
                        .orderUpdateMessage("Payment Response with " + paymentMode + "is " + paytmPaymentsService
                                .getPaymentResponseMessage(paymentCallbackRequestDto.getPaymentResponseParams()))
                        .transactionId(paymentCallbackRequestDto.getPaymentResponseParams().get("ORDERID"))
                        .paymentModeTransactionId(paymentsModeTransactionId)
                        .build();

                if (paytmTransactionStatus) {
                    LOGGER.info("Received success response from paytm moving state to :: {}",
                            OrderStateEnum.ORDER_PAYMENT_SUCCESS);
                    omsRequest.setOrderStatus(OrderStateEnum.ORDER_PAYMENT_SUCCESS.getOrderState());
                } else {
                    LOGGER.info("Received failed response from paytm moving state to :: {}",
                            OrderStateEnum.ORDER_PAYMENT_FAILED);
                    omsRequest.setOrderStatus(OrderStateEnum.ORDER_PAYMENT_FAILED.getOrderState());
                }
                OmsResponse omsResponse = omsService.updateOrderStatus(omsRequest);
                paymentCallbackResponseDto = PaymentCallbackResponseDto.builder()
                        .transactionId(omsResponse.getTransactionId())
                        .orderStatus(omsResponse.getCurrentState())
                        .totalOrderAmount(BigDecimal.valueOf(Double.valueOf(paymentCallbackRequestDto
                                .getPaymentResponseParams().get("TXNAMOUNT"))))
                        .gameCode(omsResponse.getGameCode())
                        .platformCode(omsResponse.getPlatformCode())
                        .tournamentCode(omsResponse.getTournamentCode())
                        .userCode(omsResponse.getUserCode())
                        .build();
                break;
            default:
        }

        return paymentCallbackResponseDto;
    }
}
