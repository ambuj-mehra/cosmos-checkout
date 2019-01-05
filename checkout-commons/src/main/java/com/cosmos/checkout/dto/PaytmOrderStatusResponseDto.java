package com.cosmos.checkout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The type Paytm order status response dto.
 *
 * @author ambujmehra
 */
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@ToString
public class PaytmOrderStatusResponseDto {

    @JsonProperty("TXNID")
    private String paytmTransactionId;

    @JsonProperty("BANKTXNID")
    private String paytmBankTransactionId;

    @JsonProperty("ORDERID")
    private String cosmosTransactionId;

    @JsonProperty("TXNAMOUNT")
    private String transactionAmount;

    @JsonProperty("STATUS")
    private String status;

    @JsonProperty("TXNTYPE")
    private String transactionType;

    @JsonProperty("GATEWAYNAME")
    private String gatewayName;

    @JsonProperty("RESPCODE")
    private String responseCode;

    @JsonProperty("RESPMSG")
    private String responseMsg;

    @JsonProperty("BANKNAME")
    private String bankName;

    @JsonProperty("MID")
    private String mid;

    @JsonProperty("PAYMENTMODE")
    private String paymentMode;

    @JsonProperty("REFUNDAMT")
    private String refundAmount;

    @JsonProperty("TXNDATE")
    private String transactionDate;



}
