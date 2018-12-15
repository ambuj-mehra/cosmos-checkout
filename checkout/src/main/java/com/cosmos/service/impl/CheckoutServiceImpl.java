package com.cosmos.service.impl;

import com.cosmos.auth.bean.UserAuth;
import com.cosmos.checkout.dto.InitiateCheckoutRequest;
import com.cosmos.checkout.dto.InitiateCheckoutResponse;
import com.cosmos.entity.Orders;
import com.cosmos.repository.OrdersRepository;
import com.cosmos.service.IcheckoutService;
import com.cosmos.utils.CheckoutUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    @Override
    @Transactional
    public InitiateCheckoutResponse initiateCheckout(InitiateCheckoutRequest initiateCheckoutRequest, UserAuth userAuth) {
        Orders orders = ordersRepository.save(checkoutUtils.getOrdersFromCheckoutRequest(initiateCheckoutRequest, userAuth));
        LOGGER.info("order created in checkout Db for user :: {} with orderid :: {} and trnx id :: {}", userAuth.getUserCode(), orders.getId(), orders.getTransactionId());
        return InitiateCheckoutResponse.builder()
        .orderDate(orders.getOrderDate().getTime())
                .orderStatus(orders.getOrderStatus())
                .PaymentStatus(orders.getOrderPayment().isCompleted())
                .totalOrderAmount(orders.getTotalOrderAmount())
                .transactionId(orders.getTransactionId())
                .build();
    }
}
