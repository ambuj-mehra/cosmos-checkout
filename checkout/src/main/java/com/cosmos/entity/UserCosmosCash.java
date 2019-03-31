package com.cosmos.entity;

import com.cosmos.auth.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * The type Cosmos cash.
 *
 * @author ambujmehra
 */
@Entity
@Table(name = "user_cosmos_cash")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCosmosCash extends BaseEntity {

    @Column(name = "cosmos_cash", nullable = false)
    private BigDecimal cosmosCash;

    @Column(name = "user_code", nullable = false)
    private String userCode;
}
