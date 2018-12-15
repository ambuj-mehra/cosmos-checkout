package com.cosmos.controller;

import com.cosmos.auth.bean.UserAuth;
import com.cosmos.checkout.dto.InitiateCheckoutRequest;
import com.cosmos.checkout.dto.InitiateCheckoutResponse;
import com.cosmos.service.IcheckoutService;
import com.cosmos.utils.ControllerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
     * Gets customer.
     *
     * @param request the request
     * @return the customer
     */
    @ModelAttribute("user")
    public UserAuth getCustomer(HttpServletRequest request) {
        return ControllerUtils.getCustomer(request);
    }

    @RequestMapping(value = "initiate", method = RequestMethod.POST)
    public InitiateCheckoutResponse initiateCheckout(@RequestBody InitiateCheckoutRequest initiateCheckoutRequest,
                                                     @ModelAttribute("user") UserAuth userAuth) {
        LOGGER.info("received checkout request for user :: {} for tournamant ::{} ", userAuth.getUserCode(), initiateCheckoutRequest.getTournamantCode());
        return checkoutService.initiateCheckout(initiateCheckoutRequest, userAuth);

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
