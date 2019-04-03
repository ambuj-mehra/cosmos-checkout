package com.cosmos.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

/**
 * The type Order lite.
 *
 * @author ambujmehra
 */
@Getter
@Setter
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderLite {

    private String transactionId;
    private String gameCode;
    private String platformCode;
    private String tournamentCode;
    private Integer orderStatus;
    private BigDecimal totalOrderAmount;
    private BigDecimal actualOrderAmount;
    private BigDecimal cosmosCash;
    private String userCode;
    private Long orderDate;

}
