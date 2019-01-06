package com.cosmos.test;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * The type Ledger.
 */
@Getter
@Builder
@Setter
@AllArgsConstructor
@ToString
public class Ledger {

    private String firstName;
    private String lastName;

    private List<Transaction> credits;
    private List<Transaction> debits;

    /**
     * The type Transaction.
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @ToString
    public static class Transaction {
        private String type;//CREDIT or DEBIT
        private BigDecimal amount;
        private LocalDate transactionTime; // LocalDate time example in java 8
        private String transactingPerson;

        /**
         * Gets amount for zaak pay.
         *
         * @param amount the amount
         * @return the amount for zaak pay
         */
        public static String getAmountForZaakPay(BigDecimal amount) {
            return amount.multiply(BigDecimal.valueOf(100)).toString();
        }
    }
}
