package com.cosmos.controller;

import com.cosmos.checkout.dto.PaymentCallbackRequestDto;
import com.cosmos.checkout.dto.PaymentCallbackResponseDto;
import com.cosmos.service.IcheckoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Paytm controller.
 *
 * @author ambujmehra
 */
@RestController
@RequestMapping("/payment")
public class PaymentGatewayCallbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentGatewayCallbackController.class);

    @Autowired
    private IcheckoutService checkoutService;


    /**
     * Paytm call back string.
     *
     * @param paymentCallbackRequestDto the payment callback request dto
     * @return the string
     */
    @RequestMapping(value = "/paytm/callback", method = RequestMethod.POST)
    public PaymentCallbackResponseDto paytmCallBack(@RequestBody PaymentCallbackRequestDto paymentCallbackRequestDto) {
        LOGGER.info("received response from paytm for params :: {}", paymentCallbackRequestDto.toString());
        PaymentCallbackResponseDto paymentCallbackResponseDto = checkoutService
                .initiateCallbackPayment(paymentCallbackRequestDto);
        LOGGER.info("rresponse from payment callback:: {}", paymentCallbackResponseDto.toString());
        return paymentCallbackResponseDto;
    }
}
