package com.cosmos.entity;

import com.cosmos.auth.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * The type Order state transition.
 *
 * @author ambujmehra
 */
@Entity
@Table(name = "order_state_transition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStateTransition extends BaseEntity{

    @Column(name = "order_status", nullable = false)
    private Integer orderStatus;

    @Column(name = "order_update_message", nullable = false)
    private String orderUpdateMessage;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

}
