package com.cosmos.service;

import com.cosmos.auth.bean.UserAuth;
import com.cosmos.checkout.dto.InitiateCheckoutRequest;
import com.cosmos.checkout.dto.InitiateCheckoutResponse;

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
}
