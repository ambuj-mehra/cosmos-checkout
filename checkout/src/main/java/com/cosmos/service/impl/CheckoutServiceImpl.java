package com.cosmos.service.impl;

import com.cosmos.checkout.dto.*;
import com.cosmos.checkout.enums.OrderStateEnum;
import com.cosmos.checkout.enums.PaymentMode;
import com.cosmos.checkout.enums.TransactionType;
import com.cosmos.entity.OrderPayment;
import com.cosmos.entity.Orders;
import com.cosmos.exception.CheckoutException;
import com.cosmos.repository.OrderPaymentRepository;
import com.cosmos.repository.OrdersRepository;
import com.cosmos.service.IcheckoutService;
import com.cosmos.utils.CheckoutUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @Override
    @Transactional(rollbackOn = Exception.class)
    public InitiateCheckoutResponse initiateCheckout(InitiateCheckoutRequest initiateCheckoutRequest) {
        Orders orders = ordersRepository.save(checkoutUtils.getOrdersFromCheckoutRequest(initiateCheckoutRequest));
        LOGGER.info("order created in checkout Db for user :: {} with orderid :: {} and trnx id :: {}",
                initiateCheckoutRequest.getUserCode(), orders.getId(), orders.getTransactionId());
        return InitiateCheckoutResponse.builder()
        .orderDate(orders.getOrderDate().getTime())
                .orderStatus(orders.getOrderStatus())
                .totalOrderAmount(orders.getTotalOrderAmount())
                .transactionId(orders.getTransactionId())
                .paymentOptions(Arrays.stream(PaymentMode.values())
                        .map(InitiateCheckoutResponse.PaymentOption::getFromPaymentMode)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public PaymentResponseDto initiatePayment(InitiatePaymentRequestDto initiatePaymentRequestDto) {

        Orders orders = ordersRepository.findByTransactionId(initiatePaymentRequestDto.getTransactionId());
        Optional.ofNullable(orders).orElseThrow(() -> new CheckoutException("Order not found"));
        PaymentMode paymentMode = PaymentMode.getPaymentModeById(initiatePaymentRequestDto.getPaymentModeId());
        PaymentResponseDto.PaymentOptionData paymentOptionData = null;
        switch (paymentMode) {
            case PAYTM:
                orders.setPaymentMode(PaymentMode.PAYTM.getPaymentModeId());
                 paymentOptionData = paytmPaymentsService.getPaymentsOptionData(initiatePaymentRequestDto);
                 break;
            default:
                paymentOptionData = null;
        }
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setCompleted(false);
        orderPayment.setOrder(orders);
        orderPayment.setTotalOrderAmount(initiatePaymentRequestDto.getTotalOrderAmount());// this is final discount paytm amount
        orderPayment.setTransactionType(TransactionType.DEBIT);
        orderPaymentRepository.save(orderPayment);

        OmsRequest omsRequest = OmsRequest.builder()
                .orderStatus(OrderStateEnum.ORDER_PAYMENT_INITIATE.getOrderState())
                .orderUpdateMessage("Payment initiated with" + paymentMode)
                .transactionId(initiatePaymentRequestDto.getTransactionId())
                .userCode(initiatePaymentRequestDto.getUserCode())
                .build();
        omsService.updateOrderStatus(omsRequest);
        return PaymentResponseDto.builder()
                .paymentOptionData(paymentOptionData)
                .totalAmount(initiatePaymentRequestDto.getTotalOrderAmount().toString())
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
                boolean paytmTransactionStatus = false;
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
                        .orderUpdateMessage("Payment Response with" + paymentMode)
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
                        .totalOrderAmount(Double.valueOf(paymentCallbackRequestDto.getPaymentResponseParams().get("TXNAMOUNT")))
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
