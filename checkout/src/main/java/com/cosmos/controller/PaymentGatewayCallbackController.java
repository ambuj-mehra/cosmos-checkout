package com.cosmos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * The type Paytm controller.
 *
 * @author ambujmehra
 */
@RestController
@RequestMapping("/paytm")
public class PaytmController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaytmController.class);


    @RequestMapping(value = "/callback", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String paytmCallBackTest(@RequestParam(value = "ORDER_ID") String orderId,
                                    @RequestParam Map<String, String> parammap) {
        LOGGER.info("received response from paytm");
        return "done!!";
    }
}
