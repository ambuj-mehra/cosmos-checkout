package com.cosmos.service.impl;

import com.cosmos.checkout.dto.*;
import com.cosmos.checkout.enums.OrderStateEnum;
import com.cosmos.controller.OrderDetailsController;
import com.cosmos.entity.Orders;
import com.cosmos.entity.TransactionLedger;
import com.cosmos.exception.CheckoutException;
import com.cosmos.repository.OrdersRepository;
import com.cosmos.repository.TransactionLedgerRepository;
import com.cosmos.utils.CheckoutUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private TransactionLedgerRepository transactionLedgerRepository;

    @Autowired
    private CheckoutUtils checkoutUtils;

    @Autowired
    private OmsServiceImpl omsService;

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
                .platformCode(orders.getPlatformCode())
                .totalOrderAmount(orders.getTotalOrderAmount())
                .cosmosCash(orders.getCosmosCash())
                .transactionId(transactionId)
                .tournamentCode(orders.getTournamentCode())
                .userCode(orders.getUserCode())
                .orderDate(orders.getOrderDate().getTime())
                .build();
    }


    /**
     * Update order from tournament code and user code oms response.
     *
     * @param rankingOrderUpdateRequestDto the ranking order update request dto
     * @return the oms response
     */
    @Transactional(rollbackFor = Exception.class)
    public OmsResponse updateOrderFromTournamentCodeAndUserCode(RankingOrderUpdateRequestDto rankingOrderUpdateRequestDto) {
        Orders orders = ordersRepository.findByTournamentCodeAndUserCode(rankingOrderUpdateRequestDto.getTournamentCode(),
                rankingOrderUpdateRequestDto.getUserCode());
        Optional.ofNullable(orders).orElseThrow(() -> new CheckoutException("Order not found"));
        LOGGER.info("Fetched order successfully for transaction is :: {}", orders.getTransactionId());
        OmsRequest omsRequest = OmsRequest.builder()
                .orderStatus(OrderStateEnum.ORDER_SUCCESS.getOrderState())
                .orderUpdateMessage("Prize Money Credited")
                .transactionId(orders.getTransactionId())
                .userCode(orders.getUserCode())
                .payoutAmount(rankingOrderUpdateRequestDto.getPayoutAmount())
                .build();
        return omsService.updateOrderStatus(omsRequest);
    }


    /**
     * Gets history.
     *
     * @param userCode the user code
     * @param page     the page
     * @param size     the size
     * @return the history
     */
    public List<OrderHistoryResponseDto> getuserHistory(String userCode, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<TransactionLedger> transactionLedgers =  transactionLedgerRepository.findAllByUserCode(userCode, pageable);
        return transactionLedgers
                .stream()
                .map(transactionLedger -> checkoutUtils.getFromTransactionLedger(transactionLedger))
                .collect(Collectors.toList());
    }

}
