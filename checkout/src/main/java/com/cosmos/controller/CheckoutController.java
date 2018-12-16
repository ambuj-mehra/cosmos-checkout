package com.cosmos.controller;

import com.cosmos.checkout.dto.InitiateCheckoutRequest;
import com.cosmos.checkout.dto.InitiateCheckoutResponse;
import com.cosmos.service.IcheckoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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


    @RequestMapping(value = "/initiate", method = RequestMethod.POST)
    public InitiateCheckoutResponse initiateCheckout(@RequestBody InitiateCheckoutRequest initiateCheckoutRequest) {
        LOGGER.info("received checkout request for user :: {} for tournamant ::{} ", initiateCheckoutRequest.getUserCode(), initiateCheckoutRequest.getTournamantCode());
        return checkoutService.initiateCheckout(initiateCheckoutRequest);

    }

    /**
     * Test string.
     *
     * @return the string
     */
    @RequestMapping(value = "/test/intercept", method = RequestMethod.GET)
    public String test() {
        return "Hello from COsmos@@";
    }

}
