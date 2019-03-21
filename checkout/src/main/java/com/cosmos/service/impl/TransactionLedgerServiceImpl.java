package com.cosmos.service.impl;

import com.cosmos.checkout.dto.OmsRequest;
import com.cosmos.checkout.enums.PaymentMode;
import com.cosmos.checkout.enums.TransactionState;
import com.cosmos.checkout.enums.TransactionType;
import com.cosmos.entity.TransactionLedger;
import com.cosmos.repository.TransactionLedgerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * The type Transaction ledger service.
 *
 * @author ambujmehra
 */
@Service
public class TransactionLedgerServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLedgerServiceImpl.class);

    @Autowired
    private TransactionLedgerRepository transactionLedgerRepository;

    /**
     * Add transaction ledger.
     *
     * @param omsRequest       the oms request
     * @param transactionType  the transaction type
     * @param transactionState the transaction state
     */
    @Transactional(propagation= Propagation.REQUIRED)
    public void addTransactionLedger(OmsRequest omsRequest, TransactionType transactionType,
                                     TransactionState transactionState, BigDecimal transactionAmount) {

        TransactionLedger transactionLedger = new TransactionLedger();
        transactionLedger.setPaymentModeTransactionId(omsRequest.getPaymentModeTransactionId());
        transactionLedger.setTransactionId(omsRequest.getTransactionId());
        transactionLedger.setTransactionState(transactionState);
        transactionLedger.setType(transactionType);
        transactionLedger.setTransactionMessage(omsRequest.getOrderUpdateMessage());
        transactionLedger.setUserCode(omsRequest.getUserCode());
        transactionLedger.setPaymentMode(PaymentMode.PAYTM.getPaymentModeId());
        transactionLedger.setTransactionAmount(transactionAmount);
        transactionLedgerRepository.save(transactionLedger);

    }
}
