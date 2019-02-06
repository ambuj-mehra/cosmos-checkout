package com.cosmos.utils;

import com.cosmos.auth.entity.User;
import com.cosmos.auth.repository.UserRepository;
import com.cosmos.checkout.dto.InitiateCheckoutRequest;
import com.cosmos.checkout.dto.OrderHistoryResponseDto;
import com.cosmos.checkout.dto.PaytmOrderStatusRequestDto;
import com.cosmos.checkout.dto.PaytmOrderStatusResponseDto;
import com.cosmos.checkout.enums.OrderStateEnum;
import com.cosmos.checkout.enums.PaymentMode;
import com.cosmos.entity.*;
import com.cosmos.exception.CheckoutException;
import com.cosmos.service.impl.CheckoutServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.binary.Hex;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


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

    @Autowired
    private UserRepository userRepository;

    /**
     * Gets orders from checkout request.
     *
     * @param initiateCheckoutRequest the initiate checkout request
     * @return the orders from checkout request
     */
    public Orders getOrdersFromCheckoutRequest(InitiateCheckoutRequest initiateCheckoutRequest) {
        Orders orders = new Orders();
        orders.setActualOrderAmount(initiateCheckoutRequest.getTotalOrderAmount());
        orders.setGamecode(initiateCheckoutRequest.getGameCode());
        orders.setOrderDate(new Date(System.currentTimeMillis()));
        orders.setOrderStatus(OrderStateEnum.ORDER_CREATED.getOrderState());
        orders.setPlatformCode(initiateCheckoutRequest.getPlatformCode());
        orders.setTotalOrderAmount(initiateCheckoutRequest.getTotalOrderAmount());
        orders.setTournamentCode(initiateCheckoutRequest.getTournamantCode());
        orders.setUserCode(initiateCheckoutRequest.getUserCode());
        orders.setTransactionId(generateUniqueTransactionId(initiateCheckoutRequest));

        OrderDiscount orderDiscount = new OrderDiscount();
        orderDiscount.setOrders(orders);
        orderDiscount.setOrderDiscount(null);

        OrderStateTransition orderStateTransition = new OrderStateTransition();
        orderStateTransition.setOrderStatus(OrderStateEnum.ORDER_CREATED.getOrderState());
        orderStateTransition.setOrderUpdateMessage("Order Initiated");
        orderStateTransition.setOrder(orders);

        List<OrderStateTransition> orderStateTransitions = new ArrayList<>();
        orderStateTransitions.add(orderStateTransition);

        orders.setOrderDiscount(orderDiscount);
        orders.setOrderStateTransitions(orderStateTransitions);

        return orders;
    }

    /**
     * Generate unique transaction id string.
     *
     * @param initiateCheckoutRequest the initiate checkout request
     * @return the string
     */
    public String generateUniqueTransactionId(InitiateCheckoutRequest initiateCheckoutRequest) {
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
        User user = userRepository.findByCode(initiateCheckoutRequest.getUserCode());
        Optional.ofNullable(user).orElseThrow(() -> new CheckoutException("User not found"));
        int userId = user.getId().intValue();
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
                initiateCheckoutRequest.getUserCode(), initiateCheckoutRequest.getTournamantCode(), transactionId);
        return transactionId;
    }

    /**
     * Is paytm transaction success boolean.
     *
     * @param paymentsCallbackParams      the payments callback params
     * @param paytmOrderStatusResponseDto the paytm order status response dto
     * @return the boolean
     */
    public boolean isPaytmTransactionSuccess(Map<String, String> paymentsCallbackParams,
                                             PaytmOrderStatusResponseDto paytmOrderStatusResponseDto) {
        if (paymentsCallbackParams.get("STATUS").equals("TXN_SUCCESS") && paytmOrderStatusResponseDto.getStatus().equals("TXN_SUCCESS"))
            return true;
        else
            return false;

    }

    /**
     * Gets from transaction ledger.
     *
     * @param transactionLedger the transaction ledger
     * @return the from transaction ledger
     */
    public OrderHistoryResponseDto getFromTransactionLedger(TransactionLedger transactionLedger) {
        return OrderHistoryResponseDto.builder()
                .paymentModeTransactionId(transactionLedger.getPaymentModeTransactionId())
                .transactionDate(transactionLedger.getUpdatedAt().getTime())
                .transactionId(transactionLedger.getTransactionId())
                .transactionState(transactionLedger.getTransactionState())
                .userCode(transactionLedger.getUserCode())
                .type(transactionLedger.getType())
                .transactionMessage(transactionLedger.getTransactionMessage())
                .build();
    }
}
