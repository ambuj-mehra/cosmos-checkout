package com.cosmos.utils;

import com.cosmos.auth.bean.UserAuth;
import com.cosmos.checkout.dto.InitiateCheckoutRequest;
import com.cosmos.checkout.enums.OrderStateEnum;
import com.cosmos.entity.OrderDiscount;
import com.cosmos.entity.OrderPayment;
import com.cosmos.entity.OrderStateTransition;
import com.cosmos.entity.Orders;
import com.cosmos.service.impl.CheckoutServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.binary.Hex;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Checkout utils.
 */
@Component
public class CheckoutUtils {

    private static int maxCount = 250;
    private static int count = 0;
    private static long time = 0;
    //TODO get unique machine id from redis at postConstruct
    private static Integer machineId = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutServiceImpl.class);

    /**
     * Gets orders from checkout request.
     *
     * @param initiateCheckoutRequest the initiate checkout request
     * @param userAuth                the user auth
     * @return the orders from checkout request
     */
    public Orders getOrdersFromCheckoutRequest(InitiateCheckoutRequest initiateCheckoutRequest, UserAuth userAuth) {
        Orders orders = new Orders();
        orders.setActualOrderAmount(initiateCheckoutRequest.getTotalOrderAmount());
        orders.setGamecode(initiateCheckoutRequest.getGameCode());
        orders.setOrderDate(new Date(System.currentTimeMillis()));
        orders.setOrderStatus(OrderStateEnum.ORDER_CREATED.getOrderState());
        orders.setPlatformCode(initiateCheckoutRequest.getPlatformCode());
        orders.setTotalOrderAmount(initiateCheckoutRequest.getTotalOrderAmount());
        orders.setTournamentCode(initiateCheckoutRequest.getTournamantCode());
        orders.setUserCode(userAuth.getUserCode());
        orders.setTransactionId(generateUniqueTransactionId(initiateCheckoutRequest, userAuth));

        OrderDiscount orderDiscount = new OrderDiscount();
        orderDiscount.setOrders(orders);
        orderDiscount.setOrderDiscount(null);

        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setCompleted(Boolean.FALSE);
        orderPayment.setOrders(orders);
        orderPayment.setPaymentMode(0);//0 is PAYTM, make a enum

        OrderStateTransition orderStateTransition = new OrderStateTransition();
        orderStateTransition.setOrderStatus(OrderStateEnum.ORDER_CREATED.getOrderState());
        orderStateTransition.setOrder(orders);
        List<OrderStateTransition> orderStateTransitions = new ArrayList<>();
        orderStateTransitions.add(orderStateTransition);

        orders.setOrderPayment(orderPayment);
        orders.setOrderDiscount(orderDiscount);
        orders.setOrderStateTransitions(orderStateTransitions);

        return orders;
    }

    /**
     * Generate unique transaction id string.
     *
     * @param initiateCheckoutRequest the initiate checkout request
     * @param userAuth                the user auth
     * @return the string
     */
    public String generateUniqueTransactionId(InitiateCheckoutRequest initiateCheckoutRequest, UserAuth userAuth) {
        String encodeHexString = null;
        byte[] data = new byte[12];
        long currentTime = System.currentTimeMillis();
        if (time == currentTime) {
            count++;
            if (count > maxCount) {
                maxCount = count;
            }
        } else {
            time = currentTime;
            count = 0;
        }
        data[0] = (byte) (currentTime >> 32);
        data[1] = (byte) (currentTime >> 24);
        data[2] = (byte) (currentTime >> 16);
        data[3] = (byte) (currentTime >> 8);
        data[4] = (byte) (currentTime);
        int userId = userAuth.getUserId().intValue();
        data[5] = (byte) (userId >> 32);
        data[6] = (byte) (userId >> 24);
        data[7] = (byte) (userId >> 16);
        data[8] = (byte) (userId >> 8);
        data[9] = (byte) (userId);
        data[10] = (byte) (machineId.byteValue());
        data[11] = (byte) (count);
        encodeHexString = Hex.encodeHexString(data);
        String name = initiateCheckoutRequest.getTournamantCode();
        String transactionId = name.substring(0, 1) + encodeHexString;
        LOGGER.info("transaction id generated for user :: {} and tournamanent :: {} is  :: {}",
                userAuth.getUserCode(), initiateCheckoutRequest.getTournamantCode(), transactionId);
        return transactionId;
    }
}
