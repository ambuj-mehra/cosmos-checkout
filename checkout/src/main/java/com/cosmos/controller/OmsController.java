package com.cosmos.controller;

import com.cosmos.checkout.dto.*;
import com.cosmos.service.IomsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        LOGGER.info("received request to update order :: {}", omsRequest.toString());
        OmsResponse omsResponse = omsService.updateOrderStatus(omsRequest);
        LOGGER.info("OMS response is :: {}", omsResponse.toString());
        return omsResponse;
    }

}
