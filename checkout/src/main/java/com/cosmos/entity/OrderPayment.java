package com.cosmos.entity;

import com.cosmos.auth.entity.base.BaseEntity;
import com.cosmos.checkout.enums.PaymentMode;
import com.cosmos.checkout.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The type Order payment.
 *
 * @author ambujmehra
 */
@Entity
@Table(name = "order_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPayment extends BaseEntity{

    @Column(name = "payment_amount", nullable = false)
    private BigDecimal totalOrderAmount;

    @Column(name = "payment_mode_transaction_id")
    private String paymentModeTransactionId;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "transaction_message")
    private String transactionMessage;

    @Column(name = "payment_mode")
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

}
