package com.cosmos.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

/**
 * The type Payment response dto.
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
public class PaymentResponseDto {

    private String transactionId;
    private String totalAmount;
    private PaymentOptionData paymentOptionData;


    @Getter
    @Setter
    @Builder
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentOptionData {
        private Integer paymentModeId;
        private String postingUrl;
        private Map<String, String> parameterMap;
        private String cosmosMerchantId;
    }
}
