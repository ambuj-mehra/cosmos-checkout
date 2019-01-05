package com.cosmos.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The type Paytm order status request dto.
 *
 * @author ambujmehra
 */
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
public class PaytmOrderStatusRequestDto {

    @JsonProperty("MID")
    private String mid;

    @JsonProperty("ORDERID")
    private String orderId;

    @JsonProperty("CHECKSUMHASH")
    private String checksum;
}
