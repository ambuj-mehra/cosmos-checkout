package com.cosmos.controller;

import com.cosmos.checkout.dto.OrderLite;
import com.cosmos.service.impl.OrderDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Order details controller.
 *
 * @author ambujmehra
 */
@RestController
@RequestMapping("/order")
public class OrderDetailsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsController.class);

    @Autowired
    private OrderDetailsService orderDetailsService;


    /**
     * Fetch order lite order lite.
     *
     * @param transactionId the transaction id
     * @return the order lite
     */
    @RequestMapping(value = "/{transactionId}", method = RequestMethod.GET)
    public OrderLite fetchOrderLite(@PathVariable(value = "transactionId") String transactionId) {
        LOGGER.info("received response to fetch orderlite for transactionid  :: []", transactionId);
        return orderDetailsService.fetchOrderLite(transactionId);
    }

}
