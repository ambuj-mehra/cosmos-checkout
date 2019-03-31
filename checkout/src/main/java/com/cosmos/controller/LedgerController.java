package com.cosmos.controller;

import com.cosmos.checkout.dto.InitiateCheckoutRequest;
import com.cosmos.checkout.dto.InitiateCheckoutResponse;
import com.cosmos.checkout.dto.LedgerCreditRequestDto;
import com.cosmos.service.impl.LedgerCreditService;
import com.cosmos.service.impl.OmsServiceImpl;
import com.cosmos.service.impl.TransactionLedgerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Ledger controller.
 *
 * @author ambujmehra
 */
@RestController
@RequestMapping("/ledger")
public class LedgerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    private LedgerCreditService ledgerCreditService;

    /**
     * Credt user ledger.
     *
     * @param ledgerCreditRequestDto the ledger credit request dto
     */
    @RequestMapping(value = "/credit", method = RequestMethod.POST)
    public void credtUserLedger(@RequestBody LedgerCreditRequestDto ledgerCreditRequestDto) {
        LOGGER.info("received initiate checkout request :: {}", ledgerCreditRequestDto.toString());
        ledgerCreditService.creditUserLedger(ledgerCreditRequestDto);
    }

}
