package com.cosmos.service;

import com.cosmos.checkout.dto.InitiatePaymentRequestDto;
import com.cosmos.checkout.dto.PaymentResponseDto;

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
}
