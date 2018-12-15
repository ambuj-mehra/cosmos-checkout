package com.cosmos.entity;

import com.cosmos.auth.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * The type Orders.
 *
 * @author ambujmehra
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orders extends BaseEntity{

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "game_code", nullable = false)
    private String gamecode;

    @Column(name = "platform_code", nullable = false)
    private String platformCode;

    @Column(name = "tournament_code", nullable = false)
    private String tournamentCode;

    @Column(name = "user_code", nullable = false)
    private String userCode;

    @Column(name = "order_date", nullable = false, columnDefinition = "timestamp", length = 6)
    private Date orderDate;

    @Column(name = "order_status", nullable = false)
    private Integer orderStatus;

    @Column(name = "total_order_amount", nullable = false)
    private Double totalOrderAmount;

    @Column(name = "actual_order_amount", nullable = false)
    private Double actualOrderAmount;


    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private OrderDiscount orderDiscount;

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private OrderPayment orderPayment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderStateTransition> orderStateTransitions;



}
