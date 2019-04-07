package com.cosmos.service;

import com.cosmos.checkout.dto.CosmosCashDto;

import java.math.BigDecimal;

/**
 * The interface Cosmos cash service.
 *
 * @author ambujmehra
 */
public interface ICosmosCashService {

    /**
     * Gets user cosmos cash balance.
     *
     * @param userCode the user code
     * @return the user cosmos cash balance
     */
    CosmosCashDto getUserCosmosCashBalance(String userCode);

    /**
     * Credit cosmos cash user cosmos cash.
     *
     * @param userCode   the user code
     * @param cosmosCash the cosmos cash
     * @return the user cosmos cash
     */
    CosmosCashDto creditCosmosCash(String userCode, BigDecimal cosmosCash);

    /**
     * Debit cosmos cash user cosmos cash.
     *
     * @param userCode   the user code
     * @param cosmosCash the cosmos cash
     * @return the user cosmos cash
     */
    CosmosCashDto debitCosmosCash(String userCode, BigDecimal cosmosCash);

    /**
     * Debit cosmos cash cosmos cash dto.
     *
     * @param transactionId the transaction id
     * @return the cosmos cash dto
     */
    CosmosCashDto debitCosmosCash(String transactionId);

    /**
     * Create cosmos wallet for user user cosmos cash.
     *
     * @param userCode             the user code
     * @param initialCosmosBalance the initial cosmos balance
     * @return the user cosmos cash
     */
    CosmosCashDto createCosmosWalletForUser(String userCode, BigDecimal initialCosmosBalance);

}
