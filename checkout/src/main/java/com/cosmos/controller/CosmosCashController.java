package com.cosmos.controller;

import com.cosmos.service.ICosmosCashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
