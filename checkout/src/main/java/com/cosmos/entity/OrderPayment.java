package com.cosmos.entity;

import com.cosmos.auth.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    @Column(name = "payment_mode", nullable = false)
    private Integer paymentMode;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private Orders orders;

}
