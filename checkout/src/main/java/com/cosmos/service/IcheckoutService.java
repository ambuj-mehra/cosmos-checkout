package com.cosmos.service;

import com.cosmos.auth.bean.UserAuth;
import com.cosmos.checkout.dto.*;

/**
 * The interface Icheckout service.
 *
 * @author ambujmehra
 */
public interface IcheckoutService {

    /**
     * Initiate checkout initiate checkout response.
     *
     * @param initiateCheckoutRequest the initiate checkout request
     * @return the initiate checkout response
     */
    InitiateCheckoutResponse initiateCheckout(InitiateCheckoutRequest initiateCheckoutRequest);


    /**
     * Initiate payment payment response dto.
     *
     * @param initiatePaymentRequestDto the initiate payment request dto
     * @return the payment response dto
     */
    PaymentResponseDto initiatePayment(InitiatePaymentRequestDto initiatePaymentRequestDto);

    /**
     * Initiate callback payment payment callback response dto.
     *
     * @param paymentCallbackRequestDto the payment callback request dto
     * @return the payment callback response dto
     */
    PaymentCallbackResponseDto initiateCallbackPayment(PaymentCallbackRequestDto paymentCallbackRequestDto);
}
