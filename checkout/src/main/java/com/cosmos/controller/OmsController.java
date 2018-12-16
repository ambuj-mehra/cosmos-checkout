package com.cosmos.controller;

import com.cosmos.checkout.dto.InitiateCheckoutRequest;
import com.cosmos.checkout.dto.InitiateCheckoutResponse;
import com.cosmos.checkout.dto.OmsRequest;
import com.cosmos.checkout.dto.OmsResponse;
import com.cosmos.service.IomsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Oms controller.
 *
 * @author ambujmehra
 */
@RestController
@RequestMapping("/oms")
public class OmsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmsController.class);

    @Autowired
    private IomsService omsService;

    /**
     * Update order status oms response.
     *
     * @param omsRequest the oms request
     * @return the oms response
     */
    @RequestMapping(value = "/updatestatus", method = RequestMethod.POST)
    public OmsResponse updateOrderStatus(@RequestBody OmsRequest omsRequest) {
        LOGGER.info("received request to update order :: {}, for user :: {}", omsRequest.getTransactionId(), omsRequest.getUserCode());
        return omsService.updateOrderStatus(omsRequest);
    }


}
