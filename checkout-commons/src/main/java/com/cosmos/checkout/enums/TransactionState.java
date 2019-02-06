package com.cosmos.checkout.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Transaction state.
 *
 * @author ambujmehra
 */
@AllArgsConstructor
@Getter
public enum TransactionState {

    /**
     * Success transaction state.
     */
    SUCCESS,

    /**
     * Pending transaction state.
     */
    PENDING,

    /**
     * Failed transaction state.
     */
    FAILED;
}
