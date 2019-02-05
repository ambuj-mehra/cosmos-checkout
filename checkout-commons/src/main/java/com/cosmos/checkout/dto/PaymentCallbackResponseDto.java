package com.cosmos.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * The type Payment callback response dto.
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
public class PaymentCallbackResponseDto {

    private String transactionId;
    private Double totalOrderAmount;
    private Integer orderStatus;
    private String userCode;
    private String platformCode;
    private String gameCode;
    private String tournamentCode;
}
