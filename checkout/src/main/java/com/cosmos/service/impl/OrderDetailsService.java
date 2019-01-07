package com.cosmos.service.impl;

import com.cosmos.checkout.dto.OrderLite;
import com.cosmos.controller.OrderDetailsController;
import com.cosmos.entity.Orders;
import com.cosmos.exception.CheckoutException;
import com.cosmos.repository.OrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The type Order details service.
 *
 * @author ambujmehra
 */
@Service
public class OrderDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsController.class);

    @Autowired
    private OrdersRepository ordersRepository;

    /**
     * Fetch order lite order lite.
     *
     * @param transactionId the transaction id
     * @return the order lite
     */
    public OrderLite fetchOrderLite(String transactionId) {

        Orders orders = ordersRepository.findByTransactionId(transactionId);
        Optional.ofNullable(orders).orElseThrow(() -> new CheckoutException("Order not found"));
        LOGGER.info("Fetched order successfully for transaction is :: {}", transactionId);
        return OrderLite.builder()
                .actualOrderAmount(orders.getActualOrderAmount())
                .gameCode(orders.getGamecode())
                .orderStatus(orders.getOrderStatus())
                .paymentModeId(orders.getOrderPayment().getPaymentMode())
                .platformCode(orders.getPlatformCode())
                .totalOrderAmount(orders.getTotalOrderAmount())
                .transactionId(transactionId)
                .tournamentCode(orders.getTournamentCode())
                .userCode(orders.getUserCode())
                .orderDate(orders.getOrderDate().getTime())
                .build();
    }

}
