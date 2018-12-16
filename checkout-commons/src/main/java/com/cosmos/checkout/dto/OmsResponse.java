package com.cosmos.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Oms response.
 *
 * @author ambujmehra
 */
@Getter
@Setter
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmsResponse {

    private String userCode;
    private String transactionId;
    private Integer previousState;
    private Integer currentState;


}
