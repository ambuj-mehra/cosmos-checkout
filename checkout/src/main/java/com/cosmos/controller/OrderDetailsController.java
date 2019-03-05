package com.cosmos.controller;

import com.cosmos.checkout.dto.OmsResponse;
import com.cosmos.checkout.dto.OrderHistoryResponseDto;
import com.cosmos.checkout.dto.OrderLite;
import com.cosmos.checkout.dto.RankingOrderUpdateRequestDto;
import com.cosmos.service.impl.OrderDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        LOGGER.info("received response to fetch orderlite for transactionid  :: {}", transactionId);
        OrderLite orderLite = orderDetailsService.fetchOrderLite(transactionId);
        LOGGER.info("orderLite tem is ::{} ", orderLite.toString());
        return orderLite;
    }


    /**
     * Fetch order lite from tournament and user code oms response.
     *
     * @param rankingOrderUpdateRequestDto the ranking order update request dto
     * @return the oms response
     */
    @RequestMapping(value = "/ranking/update", method = RequestMethod.PUT)
    public OmsResponse fetchOrderLiteFromTournamentAndUserCode(@RequestBody RankingOrderUpdateRequestDto rankingOrderUpdateRequestDto) {
        LOGGER.info("received response to fetch orderlite for tournamentCode  :: {} and userCode :: {}",
                rankingOrderUpdateRequestDto.getTournamentCode(), rankingOrderUpdateRequestDto.getUserCode());
        return orderDetailsService.updateOrderFromTournamentCodeAndUserCode(rankingOrderUpdateRequestDto);
    }

    /**
     * Gets user history.
     *
     * @param userCode the user code
     * @param page     the page
     * @param size     the size
     * @return the user history
     */
    @RequestMapping(value = "/{userCode}/history", method = RequestMethod.GET)
    public List<OrderHistoryResponseDto> getUserHistory(@PathVariable(value = "userCode") String userCode,
                                                        @RequestParam(value = "page") Integer page,
                                                        @RequestParam(value = "size") Integer size) {
        LOGGER.info("received request for  trnx history for :: {}, page :: {}, size :: {}", userCode, page, size);
        List<OrderHistoryResponseDto> orderHistoryResponseDtos = orderDetailsService.getuserHistory(userCode, page, size);
        LOGGER.info("response for order history :: {}", orderHistoryResponseDtos.toString());
        return orderHistoryResponseDtos;
    }


}
