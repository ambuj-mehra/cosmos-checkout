package com.cosmos.entity;

import com.cosmos.auth.entity.base.BaseEntity;
import com.cosmos.checkout.enums.TransactionState;
import com.cosmos.checkout.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * The type Transaction ledger.
 *
 * @author ambujmehra
 */
@Entity
@Table(name = "transaction_ledger")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLedger extends BaseEntity {

    @Column(name = "user_code", nullable = false)
    private String userCode;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "payment_mode_transaction_id", nullable = false)
    private String paymentModeTransactionId;

    @Column(name = "transaction_message")
    private String transactionMessage;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType type;


    @Column(name = "transaction_state")
    @Enumerated(EnumType.STRING)
    private TransactionState transactionState;

}
