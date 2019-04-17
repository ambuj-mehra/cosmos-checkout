package com.cosmos.service.impl;

import com.cosmos.checkout.dto.OmsRequest;
import com.cosmos.checkout.dto.OmsResponse;
import com.cosmos.checkout.enums.OrderStateEnum;
import com.cosmos.checkout.enums.PaymentMode;
import com.cosmos.checkout.enums.TransactionState;
import com.cosmos.checkout.enums.TransactionType;
import com.cosmos.entity.OrderPayment;
import com.cosmos.entity.OrderStateTransition;
import com.cosmos.entity.Orders;
import com.cosmos.exception.CheckoutException;
import com.cosmos.repository.OrdersRepository;
import com.cosmos.service.ICosmosCashService;
import com.cosmos.service.IomsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Autowired
    private ICosmosCashService cosmosCashService;

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
            // REFACTOR
            if (currentState.equals(OrderStateEnum.ORDER_PAYMENT_INITIATE) && (updateStateRequest.equals(
                    OrderStateEnum.ORDER_PAYMENT_SUCCESS) || updateStateRequest.equals(OrderStateEnum.ORDER_PAYMENT_FAILED))) {
                if (updateStateRequest.equals(OrderStateEnum.ORDER_PAYMENT_SUCCESS)) {
                    List<OrderPayment> orderPayments = orders.getOrderPayments();
                    List<OrderPayment> externalOrderPayments = orderPayments.stream()
                            .filter(orderPayment1 -> orderPayment1.getTransactionType().equals(TransactionType.DEBIT)
                                    && orderPayment1.getPaymentMode().equals(PaymentMode.PAYTM))
                            .collect(Collectors.toList());

                    List<OrderPayment> internalOrderPayments = orderPayments.stream()
                            .filter(orderPayment1 -> orderPayment1.getTransactionType().equals(TransactionType.DEBIT)
                                    && orderPayment1.getPaymentMode().equals(PaymentMode.COSMOS_CASH))
                            .collect(Collectors.toList());

                    if (externalOrderPayments.size() == 1) {
                        OrderPayment externalOrderPayment = externalOrderPayments.get(0);
                        externalOrderPayment.setPaymentModeTransactionId(omsRequest.getPaymentModeTransactionId());
                        externalOrderPayment.setTransactionMessage(omsRequest.getOrderUpdateMessage());
                        externalOrderPayment.setCompleted(true);
                        transactionLedgerService.addTransactionLedger(omsRequest, TransactionType.DEBIT,
                                TransactionState.SUCCESS, orders.getActualOrderAmount(), PaymentMode.PAYTM,
                                omsRequest.getOrderUpdateMessage());
                    }

                    if (internalOrderPayments.size() == 1) {
                        OrderPayment internalOrderPayment = internalOrderPayments.get(0);
                        internalOrderPayment.setTransactionMessage("Cosmos Balance Debited");
                        internalOrderPayment.setCompleted(true);
                        // for internal cosmos ledger transaction id is same
                        internalOrderPayment.setPaymentModeTransactionId(omsRequest.getTransactionId());
                        transactionLedgerService.addTransactionLedger(omsRequest, TransactionType.DEBIT,
                                TransactionState.SUCCESS, orders.getCosmosCash(), PaymentMode.COSMOS_CASH,
                                "Cosmos Cash debit success");
                    }
                    orders.setOrderPayments(orderPayments);
                } else if (updateStateRequest.equals(OrderStateEnum.ORDER_PAYMENT_FAILED)) {
                    if (orders.getActualOrderAmount().compareTo(BigDecimal.ZERO) > 0)
                        transactionLedgerService.addTransactionLedger(omsRequest, TransactionType.DEBIT,
                            TransactionState.FAILED, orders.getActualOrderAmount(), PaymentMode.PAYTM,
                                omsRequest.getOrderUpdateMessage());
                    if (orders.getCosmosCash().compareTo(BigDecimal.ZERO) > 0)
                        transactionLedgerService.addTransactionLedger(omsRequest, TransactionType.DEBIT,
                            TransactionState.FAILED, orders.getCosmosCash(), PaymentMode.COSMOS_CASH,
                                "Cosmos cash debit failed");
                }
            }
            if (updateStateRequest.equals(OrderStateEnum.ORDER_SUCCESS)) {
                List<OrderPayment> orderPayments = orders.getOrderPayments();
                OrderPayment orderPayment = new OrderPayment();
                orderPayment.setCompleted(true);
                orderPayment.setOrder(orders);
                orderPayment.setTotalOrderAmount(omsRequest.getPayoutAmount());
                orderPayment.setPaymentMode(PaymentMode.COSMOS_CASH);
                orderPayment.setTransactionType(TransactionType.CREDIT);
                orderPayments.add(orderPayment);
                orders.setOrderPayments(orderPayments);
                cosmosCashService.creditCosmosCash(omsRequest.getUserCode(), omsRequest.getPayoutAmount());
                transactionLedgerService.addTransactionLedger(omsRequest, TransactionType.CREDIT,
                        TransactionState.SUCCESS, omsRequest.getPayoutAmount(), PaymentMode.COSMOS_CASH,
                        "Cosmos cash credit success");
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
