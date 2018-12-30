package com.cosmos.service.impl;

import com.cosmos.auth.bean.UserAuth;
import com.cosmos.checkout.dto.InitiateCheckoutRequest;
import com.cosmos.checkout.dto.InitiateCheckoutResponse;
import com.cosmos.checkout.dto.InitiatePaymentRequestDto;
import com.cosmos.checkout.dto.PaymentResponseDto;
import com.cosmos.checkout.enums.OrderStateEnum;
import com.cosmos.checkout.enums.PaymentMode;
import com.cosmos.entity.OrderPayment;
import com.cosmos.entity.OrderStateTransition;
import com.cosmos.entity.Orders;
import com.cosmos.exception.CheckoutException;
import com.cosmos.repository.OrdersRepository;
import com.cosmos.service.IcheckoutService;
import com.cosmos.utils.CheckoutUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private PaytmPaymentsService paytmPaymentsService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public InitiateCheckoutResponse initiateCheckout(InitiateCheckoutRequest initiateCheckoutRequest) {
        Orders orders = ordersRepository.save(checkoutUtils.getOrdersFromCheckoutRequest(initiateCheckoutRequest));
        LOGGER.info("order created in checkout Db for user :: {} with orderid :: {} and trnx id :: {}", initiateCheckoutRequest.getUserCode(), orders.getId(), orders.getTransactionId());
        return InitiateCheckoutResponse.builder()
        .orderDate(orders.getOrderDate().getTime())
                .orderStatus(orders.getOrderStatus())
                .PaymentStatus(orders.getOrderPayment().isCompleted())
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
                 paymentOptionData = paytmPaymentsService.getPaymentsOptionData(initiatePaymentRequestDto);
                 break;
            default:
                paymentOptionData = null;
        }

        OrderPayment orderPayment = orders.getOrderPayment();
        orderPayment.setPaymentMode(initiatePaymentRequestDto.getPaymentModeId());
        orders.setOrderPayment(orderPayment);

        orders.setOrderStatus(OrderStateEnum.ORDER_PAYMENT_INITIATE.getOrderState());

        OrderStateTransition orderStateTransition = new OrderStateTransition();
        orderStateTransition.setOrderUpdateMessage("Payment initiated with Paytm");
        orderStateTransition.setOrder(orders);
        orderStateTransition.setOrderStatus(OrderStateEnum.ORDER_PAYMENT_INITIATE.getOrderState());
        orders.getOrderStateTransitions().add(orderStateTransition);

        ordersRepository.save(orders);
        return PaymentResponseDto.builder()
                .paymentOptionData(paymentOptionData)
                .totalAmount(initiatePaymentRequestDto.getTotalOrderAmount().toString())
                .transactionId(initiatePaymentRequestDto.getTransactionId())
                .build();
    }
}
