package com.cosmos.service;

import com.cosmos.checkout.dto.InitiatePaymentRequestDto;
import com.cosmos.checkout.dto.PaymentResponseDto;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Map;

/**
 * The interface Payment mode data service.
 *
 * @author ambujmehra
 */
public interface IPaymentDetailsService {

    /**
     * Gets payments option data.
     *
     * @param initiatePaymentRequestDto the initiate payment request dto
     * @return the payments option data
     */
    PaymentResponseDto.PaymentOptionData getPaymentsOptionData(InitiatePaymentRequestDto initiatePaymentRequestDto);

    /**
     * Gets order id from callback params.
     *
     * @param paymentsCallbackParams the payments callback params
     * @return the order id from callback params
     */
    String getCosmosTransactionIdFromCallbackParams(Map<String, String> paymentsCallbackParams);

    /**
     * Gets payment mode transaction id.
     *
     * @param paymentsCallbackParams the payments callback params
     * @return the payment mode transaction id
     */
    String getPaymentModeTransactionId(Map<String, String> paymentsCallbackParams);

    /**
     * Validate callback checksum boolean.
     *
     * @param paymentsCallbackParams the payments callback params
     * @return the boolean
     */
    void validateCallbackChecksum(Map<String, String> paymentsCallbackParams);

    /**
     * Check orderstatus from payment mode boolean.
     *
     * @param paymentsCallbackParams the payments callback params
     * @return the boolean
     */
    boolean checkOrderstatusFromPaymentMode(Map<String, String> paymentsCallbackParams);
}
