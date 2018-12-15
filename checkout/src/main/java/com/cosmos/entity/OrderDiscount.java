package com.cosmos.entity;


import com.cosmos.auth.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * The type Order discount.
 *
 * @author ambujmehra
 */
@Entity
@Table(name = "order_discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDiscount extends BaseEntity{

    @Column(name = "order_discount")
    private Double orderDiscount;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private Orders orders;


}
