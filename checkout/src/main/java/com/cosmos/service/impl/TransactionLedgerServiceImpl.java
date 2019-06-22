package com.cosmos.service.impl;

import com.cosmos.checkout.dto.InitiateCheckoutRequest;
import com.cosmos.checkout.dto.OmsRequest;
import com.cosmos.checkout.enums.PaymentMode;
import com.cosmos.checkout.enums.TransactionState;
import com.cosmos.checkout.enums.TransactionType;
import com.cosmos.entity.TransactionLedger;
import com.cosmos.repository.TransactionLedgerRepository;
import com.cosmos.utils.CheckoutUtils;
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

    @Autowired
    private CheckoutUtils checkoutUtils;

    /**
     * Add transaction ledger.
     *
     * @param omsRequest         the oms request
     * @param transactionType    the transaction type
     * @param transactionState   the transaction state
     * @param transactionAmount  the transaction amount
     * @param paymentMode        payment mode
     * @param transactionMessage tranx message
     */
    @Transactional(propagation= Propagation.REQUIRED)
    public void addTransactionLedger(OmsRequest omsRequest, TransactionType transactionType,
                                     TransactionState transactionState, BigDecimal transactionAmount,
                                     PaymentMode paymentMode, String transactionMessage) {

        TransactionLedger transactionLedger = new TransactionLedger();
        transactionLedger.setPaymentModeTransactionId(omsRequest.getPaymentModeTransactionId());
        transactionLedger.setTransactionId(omsRequest.getTransactionId());
        transactionLedger.setTransactionState(transactionState);
        transactionLedger.setType(transactionType);
        transactionLedger.setTransactionMessage(transactionMessage);
        transactionLedger.setUserCode(omsRequest.getUserCode());
        transactionLedger.setPaymentMode(paymentMode.getPaymentModeId());
        transactionLedger.setTransactionAmount(transactionAmount);
        transactionLedgerRepository.save(transactionLedger);

    }


    /**
     * Add transaction ledger.
     *
     * @param userCode           the user code
     * @param transactionName    the transaction name
     * @param transactionType    the transaction type
     * @param transactionState   the transaction state
     * @param transactionAmount  the transaction amount
     * @param paymentMode        the payment mode
     * @param transactionMessage the transaction message
     */
    @Transactional(propagation= Propagation.REQUIRED)
    public void addTransactionLedger(String userCode, String transactionName, TransactionType transactionType,
                                     TransactionState transactionState, BigDecimal transactionAmount,
                                     PaymentMode paymentMode, String transactionMessage) {
        TransactionLedger transactionLedger = new TransactionLedger();
        InitiateCheckoutRequest initiateCheckoutRequest = new InitiateCheckoutRequest();
        initiateCheckoutRequest.setUserCode(userCode);
        initiateCheckoutRequest.setTournamantCode(transactionName);
        String transactionId = checkoutUtils.generateUniqueTransactionId(initiateCheckoutRequest);
        transactionLedger.setPaymentModeTransactionId(transactionId);
        transactionLedger.setTransactionId(transactionId);
        transactionLedger.setTransactionState(transactionState);
        transactionLedger.setType(transactionType);
        transactionLedger.setTransactionMessage(transactionMessage);
        transactionLedger.setUserCode(userCode);
        transactionLedger.setPaymentMode(paymentMode.getPaymentModeId());
        transactionLedger.setTransactionAmount(transactionAmount);
        transactionLedgerRepository.save(transactionLedger);

    }

}
