package com.cosmos.service;

import com.cosmos.entity.UserCosmosCash;

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
    UserCosmosCash getUserCosmosCashBalance(String userCode);

    /**
     * Credit cosmos cash user cosmos cash.
     *
     * @param userCode   the user code
     * @param cosmosCash the cosmos cash
     * @return the user cosmos cash
     */
    UserCosmosCash creditCosmosCash(String userCode, BigDecimal cosmosCash);

    /**
     * Debit cosmos cash user cosmos cash.
     *
     * @param userCode   the user code
     * @param cosmosCash the cosmos cash
     * @return the user cosmos cash
     */
    UserCosmosCash debitCosmosCash(String userCode, BigDecimal cosmosCash);

    /**
     * Create cosmos wallet for user user cosmos cash.
     *
     * @param userCode             the user code
     * @param initialCosmosBalance the initial cosmos balance
     * @return the user cosmos cash
     */
    UserCosmosCash createCosmosWalletForUser(String userCode, BigDecimal initialCosmosBalance);

}
