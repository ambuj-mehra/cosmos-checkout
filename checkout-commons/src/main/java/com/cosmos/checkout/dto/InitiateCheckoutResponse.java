package com.cosmos.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.*;

/**
 * The type Initiate checkout response.
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
public class InitiateCheckoutResponse {

    private String transactionId;
    private Double totalOrderAmount;
    private Integer orderStatus;
    private Long orderDate;
    private Boolean PaymentStatus;

}
