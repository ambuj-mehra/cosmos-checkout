package com.cosmos.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * The type Initiate payment request dto.
 *
 * @author ambujmehra
 */
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@ToString
public class InitiatePaymentRequestDto {

    private String transactionId;
    private BigDecimal totalOrderAmount;
    private BigDecimal actualOrderAmount;
    private BigDecimal cosmosCashUsed;
    private Integer paymentModeId; //  external payment mode
    private String userCode;
}
