package com.cosmos.checkout.dto;

import com.cosmos.checkout.enums.TransactionState;
import com.cosmos.checkout.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


/**
 * The type Order history response dto.
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
public class OrderHistoryResponseDto {

    private String userCode;
    private String transactionId;
    private String paymentModeTransactionId;
    private TransactionType type;
    private TransactionState transactionState;
    private Long transactionDate;
    private String transactionMessage;
    private Integer paymentMode;
    private Double transactionAmount;

}
