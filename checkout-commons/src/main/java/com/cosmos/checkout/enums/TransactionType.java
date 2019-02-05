package com.cosmos.checkout.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * The enum Transaction type.
 *
 * @author ambujmehra
 */
@AllArgsConstructor
@Getter
public enum TransactionType {

    /**
     * Credit transaction type.
     */
    CREDIT,

    /**
     * Debit transaction type.
     */
    DEBIT

}
