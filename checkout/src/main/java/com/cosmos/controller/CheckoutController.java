package com.cosmos.controller;

import com.cosmos.checkout.dto.InitiateCheckoutRequest;
import com.cosmos.checkout.dto.InitiateCheckoutResponse;
import com.cosmos.checkout.dto.OrderLite;
import com.cosmos.service.IcheckoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * The type Checkout controller.
 *
 * @author ambujmehra
 */
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    private IcheckoutService checkoutService;


    /**
     * Initiate checkout initiate checkout response.
     *
     * @param initiateCheckoutRequest the initiate checkout request
     * @return the initiate checkout response
     */
    @RequestMapping(value = "/initiate", method = RequestMethod.POST)
    public InitiateCheckoutResponse initiateCheckout(@RequestBody InitiateCheckoutRequest initiateCheckoutRequest) {
        LOGGER.info("received checkout request for user :: {} for tournamant ::{} ", initiateCheckoutRequest.getUserCode(), initiateCheckoutRequest.getTournamantCode());
        return checkoutService.initiateCheckout(initiateCheckoutRequest);

    }

}
