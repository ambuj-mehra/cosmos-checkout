package com.cosmos.checkout.dto;

import com.cosmos.checkout.enums.PaymentMode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.*;

import java.util.List;

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
    private List<PaymentOption> paymentOptions;

    /**
     * The type Payment option.
     */
    @Getter
    @Setter
    @Builder
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentOption {
        private Integer paymentModeId;
        private String paymentModeLogo;

        /**
         * Gets from payment mode.
         *
         * @param paymentMode the payment mode
         * @return the from payment mode
         */
        public static PaymentOption getFromPaymentMode(PaymentMode paymentMode) {
            PaymentOption paymentOption = new PaymentOption();
            paymentOption.setPaymentModeId(paymentMode.getPaymentModeId());
            paymentOption.setPaymentModeLogo(paymentMode.getPaymentModeLogo());
            return paymentOption;
        }
    }

}
