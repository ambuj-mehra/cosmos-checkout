package com.cosmos.controller;

import com.cosmos.checkout.dto.CosmosCashDto;
import com.cosmos.checkout.dto.OrderLite;
import com.cosmos.service.ICosmosCashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Cosmos cash controller.
 *
 * @author ambujmehra
 */
@RestController
@RequestMapping("/cosmoscash")
public class CosmosCashController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CosmosCashController.class);

    @Autowired
    private ICosmosCashService cosmosCashService;

    /**
     * Gets cosmos cash.
     *
     * @param userCode the user code
     * @return the cosmos cash
     */
    @RequestMapping(value = "/{usercode}", method = RequestMethod.GET)
    public CosmosCashDto getCosmosCash(@PathVariable(value = "usercode") String userCode) {
        LOGGER.info("received response to fetch cosmos cash for usercode  :: {}", userCode);
        CosmosCashDto cosmosCashDto = cosmosCashService.getUserCosmosCashBalance(userCode);
        LOGGER.info("cosmos cash tem is ::{} ", cosmosCashDto.toString());
        return cosmosCashDto;
    }

}
