package com.cosmos.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

/**
 * The type Oms request.
 *
 * @author ambujmehra
 */
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
@ToString
public class OmsRequest {

    private String userCode;
    private String transactionId;
    private Integer orderStatus;
    private String orderUpdateMessage;
    private String paymentModeTransactionId;
    private BigDecimal payoutAmount;
}
