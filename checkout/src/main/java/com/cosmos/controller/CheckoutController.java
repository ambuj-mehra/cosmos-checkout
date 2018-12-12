package com.cosmos.controller;

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
