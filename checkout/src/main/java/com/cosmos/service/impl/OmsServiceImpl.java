package com.cosmos.service.impl;

import com.cosmos.checkout.dto.OmsRequest;
import com.cosmos.checkout.dto.OmsResponse;
import com.cosmos.checkout.enums.OrderStateEnum;
import com.cosmos.controller.OmsController;
import com.cosmos.entity.OrderPayment;
import com.cosmos.entity.OrderStateTransition;
import com.cosmos.entity.Orders;
import com.cosmos.exception.CheckoutException;
import com.cosmos.repository.OrdersRepository;
import com.cosmos.service.IomsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * The type Oms service.
 *
 * @author ambujmehra
 */
@Service
public class OmsServiceImpl implements IomsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmsServiceImpl.class);

    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public OmsResponse updateOrderStatus(OmsRequest omsRequest) {
        Orders orders = ordersRepository.findByTransactionId(omsRequest.getTransactionId());
        Optional.ofNullable(orders).orElseThrow(() -> new CheckoutException("Order not found"));
        if (omsRequest.getUserCode() == null) {
            omsRequest.setUserCode(orders.getUserCode());
        }
        OrderStateEnum currentState = OrderStateEnum.orderStateMap.get(orders.getOrderStatus());
        OrderStateEnum updateStateRequest = OrderStateEnum.orderStateMap.get(omsRequest.getOrderStatus());

        if (OrderStateEnum.isValidStateTransition(currentState, updateStateRequest)) {
            orders.setOrderStatus(updateStateRequest.getOrderState());
            OrderStateTransition orderStateTransition = new OrderStateTransition();
            orderStateTransition.setOrderUpdateMessage(omsRequest.getOrderUpdateMessage());
            orderStateTransition.setOrder(orders);
            orderStateTransition.setOrderStatus(updateStateRequest.getOrderState());
            orders.getOrderStateTransitions().add(orderStateTransition);
            if (currentState.equals(OrderStateEnum.ORDER_PAYMENT_INITIATE) && updateStateRequest.equals(
                    OrderStateEnum.ORDER_PAYMENT_SUCCESS)) {
                OrderPayment orderPayment = orders.getOrderPayment();
                orderPayment.setCompleted(true);
                orders.setOrderPayment(orderPayment);
            }
            ordersRepository.save(orders);
            return OmsResponse.builder()
                    .currentState(updateStateRequest.getOrderState())
                    .previousState(currentState.getOrderState())
                    .transactionId(omsRequest.getTransactionId())
                    .userCode(omsRequest.getUserCode())
                    .gameCode(orders.getGamecode())
                    .platformCode(orders.getPlatformCode())
                    .tournamentCode(orders.getTournamentCode())
                    .build();
        } else {
            throw new CheckoutException("invalid state movement detected for transactionid ::" +
                    omsRequest.getTransactionId());
        }

    }
}
