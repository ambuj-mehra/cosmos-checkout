package com.cosmos.service.impl;

import com.cosmos.checkout.dto.LedgerCreditRequestDto;
import com.cosmos.checkout.dto.OmsRequest;
import com.cosmos.checkout.enums.OrderStateEnum;
import com.cosmos.checkout.enums.PaymentMode;
import com.cosmos.checkout.enums.TransactionState;
import com.cosmos.checkout.enums.TransactionType;
import com.cosmos.entity.Orders;
import com.cosmos.exception.CheckoutException;
import com.cosmos.repository.OrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The type Ledger credit service.
 *
 * @author ambujmehra
 */
@Service
public class LedgerCreditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerCreditService.class);


    @Autowired
    private OmsServiceImpl omsService;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private TransactionLedgerServiceImpl transactionLedgerService;

    /**
     * Credit user ledger.
     *
     * @param ledgerCreditRequestDto the ledger credit request dto
     */
    public void creditUserLedger(LedgerCreditRequestDto ledgerCreditRequestDto) {
        Orders orders = ordersRepository.findByTournamentCodeAndUserCodeAndOrderStatus(ledgerCreditRequestDto.getTournamentCode(),
                ledgerCreditRequestDto.getUserCode(), OrderStateEnum.ORDER_TOURNAMNET_JOINED.getOrderState());
        Optional.ofNullable(orders).orElseThrow(() -> new CheckoutException("Order not found"));
        LOGGER.info("Fetched order successfully for transaction is :: {}", orders.getTransactionId());
        OmsRequest omsRequest = OmsRequest.builder()
                .orderUpdateMessage("Tournament payout completed")
                .orderStatus(OrderStateEnum.ORDER_SUCCESS.getOrderState())
                .transactionId(orders.getTransactionId())
                .userCode(orders.getUserCode())
                .payoutAmount(ledgerCreditRequestDto.getPrizeMoney())
                .build();
        omsService.updateOrderStatus(omsRequest);

    }


    /**
     * Credit user external payment ledger.
     *
     * @param ledgerCreditRequestDto the ledger credit request dto
     */
    public void creditUserExternalPaymentLedger(LedgerCreditRequestDto ledgerCreditRequestDto) {
        transactionLedgerService.addTransactionLedger(ledgerCreditRequestDto.getUserCode(), "PAYTM_CREDIT", TransactionType.CREDIT,
                TransactionState.SUCCESS, ledgerCreditRequestDto.getPrizeMoney(), PaymentMode.PAYTM, "Paytm wallet credit");
    }

}
