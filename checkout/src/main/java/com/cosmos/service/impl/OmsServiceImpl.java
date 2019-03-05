package com.cosmos.service.impl;

import com.cosmos.checkout.dto.OmsRequest;
import com.cosmos.checkout.dto.OmsResponse;
import com.cosmos.checkout.enums.OrderStateEnum;
import com.cosmos.checkout.enums.TransactionState;
import com.cosmos.checkout.enums.TransactionType;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private TransactionLedgerServiceImpl transactionLedgerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
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
            if (currentState.equals(OrderStateEnum.ORDER_PAYMENT_INITIATE) && (updateStateRequest.equals(
                    OrderStateEnum.ORDER_PAYMENT_SUCCESS) || updateStateRequest.equals(OrderStateEnum.ORDER_PAYMENT_FAILED))) {
                List<OrderPayment> orderPayments = orders.getOrderPayments();
                OrderPayment orderPayment = orderPayments.stream()
                        .filter(orderPayment1 -> orderPayment1.getTransactionType().equals(TransactionType.DEBIT))
                        .collect(Collectors.toList()).get(0);
                orderPayment.setPaymentModeTransactionId(omsRequest.getPaymentModeTransactionId());
                orderPayment.setTransactionMessage(omsRequest.getOrderUpdateMessage());
                if (updateStateRequest.equals(OrderStateEnum.ORDER_PAYMENT_FAILED)) {
                    transactionLedgerService.addTransactionLedger(omsRequest, TransactionType.DEBIT,
                            TransactionState.FAILED, orders.getTotalOrderAmount());
                } else if (updateStateRequest.equals(OrderStateEnum.ORDER_PAYMENT_SUCCESS)){
                    orderPayment.setCompleted(true);
                    transactionLedgerService.addTransactionLedger(omsRequest, TransactionType.DEBIT,
                            TransactionState.SUCCESS, orders.getTotalOrderAmount());
                }
                orders.setOrderPayments(orderPayments);
            }
            if (updateStateRequest.equals(OrderStateEnum.ORDER_SUCCESS)) {
                List<OrderPayment> orderPayments = orders.getOrderPayments();
                OrderPayment orderPayment = new OrderPayment();
                orderPayment.setCompleted(true);
                orderPayment.setOrder(orders);
                orderPayment.setTotalOrderAmount(omsRequest.getPayoutAmount());
                orderPayment.setTransactionType(TransactionType.CREDIT);
                orderPayments.add(orderPayment);
                orders.setOrderPayments(orderPayments);
                transactionLedgerService.addTransactionLedger(omsRequest, TransactionType.CREDIT,
                        TransactionState.SUCCESS, omsRequest.getPayoutAmount());

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
