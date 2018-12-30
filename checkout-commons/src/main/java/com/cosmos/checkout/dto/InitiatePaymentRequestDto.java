package com.cosmos.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class InitiatePaymentRequestDto {

    private String transactionId;
    private Double totalOrderAmount;
    private Integer paymentModeId;
    private String userCode;
}
