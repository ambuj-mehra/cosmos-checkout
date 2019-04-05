package com.cosmos.controller;

import com.cosmos.checkout.dto.CosmosCashCreateRequestDto;
import com.cosmos.checkout.dto.CosmosCashDto;
import com.cosmos.checkout.dto.OrderLite;
import com.cosmos.service.ICosmosCashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Create cosmos cash wallet cosmos cash dto.
     *
     * @param cosmosCashCreateRequestDto the cosmos cash create request dto
     * @return the cosmos cash dto
     */
    @RequestMapping(value ="/create", method = RequestMethod.POST)
    public CosmosCashDto createCosmosCashWallet(@RequestBody CosmosCashCreateRequestDto cosmosCashCreateRequestDto) {
        LOGGER.info("received request to generate cosmosm cash for user  :: {}", cosmosCashCreateRequestDto.getUserCode());
        CosmosCashDto cosmosCashDto = cosmosCashService.createCosmosWalletForUser(cosmosCashCreateRequestDto.getUserCode(),
                cosmosCashCreateRequestDto.getInitialCosmosCash());
        LOGGER.info("Cosmos cash created is :: {}", cosmosCashDto.toString()
        );
        return cosmosCashDto;
    }

}
