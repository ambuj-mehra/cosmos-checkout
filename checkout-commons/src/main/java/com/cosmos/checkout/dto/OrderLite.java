package com.cosmos.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

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
public class OrderLite {

    private String transactionId;
    private String gameCode;
    private String platformCode;
    private String tournamentCode;
    private Integer orderStatus;
    private Double totalOrderAmount;
    private Double actualOrderAmount;
    private Integer paymentModeId;

}
