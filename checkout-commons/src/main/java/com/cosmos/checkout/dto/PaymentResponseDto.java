package com.cosmos.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
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
@ToString
public class PaymentResponseDto {

    private String transactionId;
    private BigDecimal totalOrderAmount;
    private BigDecimal actualOrderAmount;
    private BigDecimal cosmosCash;
    private PaymentOptionData paymentOptionData;


    @Getter
    @Setter
    @Builder
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class PaymentOptionData {
        private Integer paymentModeId;
        private String postingUrl;
        private Map<String, String> parameterMap;
        private String cosmosMerchantId;
    }
}
