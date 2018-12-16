package com.cosmos.checkout.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The enum Order state enum.
 *
 * @author ambujmehra
 */
@AllArgsConstructor
@Getter
public enum OrderStateEnum {

    /**
     * The Order created.
     */
    ORDER_CREATED(0, "Order created on cosmos DB", Boolean.FALSE),

    /**
     * The Order loyality applied.
     */
    ORDER_LOYALITY_APPLIED(1, "Loyality discount applied", Boolean.FALSE),

    /**
     * The Order payment initiate.
     */
    ORDER_PAYMENT_INITIATE(3, "Order send for payment", Boolean.FALSE),

    /**
     * The Order payment success.
     */
    ORDER_PAYMENT_SUCCESS(4, "Payment Collected successfully", Boolean.FALSE),

    /**
     * The Order payment failed.
     */
    ORDER_PAYMENT_FAILED(5, "Payment Failed", Boolean.TRUE),

    /**
     * The Order tournament join initiate.
     */
    ORDER_TOURNAMENT_JOIN_INITIATE(6, "Initiate User join Tournament", Boolean.FALSE),

    /**
     * The Order failed.
     */
    ORDER_FAILED(13, "Order failed, refund", Boolean.TRUE),

    /**
     * The Order tournamnet joined.
     */
    ORDER_TOURNAMNET_JOINED(7, "tournamnt joined, poll data", Boolean.FALSE),

    /**
     * The Order tournament start.
     */
    ORDER_TOURNAMENT_START(8, "Data update pre tournamnt", Boolean.FALSE),

    /**
     * The Order tournament end.
     */
    ORDER_TOURNAMENT_END(9, "Data update post tournamnt", Boolean.FALSE),

    /**
     * The Order prize processed.
     */
    ORDER_PRIZE_PROCESSED(10, "prize calculation completed", Boolean.FALSE),

    /**
     * The Order success.
     */
    ORDER_SUCCESS(11, "Amount Credited", Boolean.TRUE),

    /**
     * The Order credit failed.
     */
    ORDER_CREDIT_FAILED(12, "Retry prize credit", Boolean.FALSE);


    private Integer orderState;
    private String OrderStateDesc;
    private Boolean isTerminalState;


    /**
     * The constant stateMovementList.
     */
    public static HashMap<OrderStateEnum, List<OrderStateEnum>> stateMovementList = new HashMap<>();

    /**
     * The Order state map.
     */
    public static Map<Integer, OrderStateEnum> orderStateMap;

    static {
        stateMovementList.put(ORDER_CREATED, new ArrayList<OrderStateEnum>() {
            {
                add(ORDER_LOYALITY_APPLIED);
            }
        });


        stateMovementList.put(ORDER_LOYALITY_APPLIED, new ArrayList<OrderStateEnum>() {
            {
                add(ORDER_PAYMENT_INITIATE);
            }
        });


        stateMovementList.put(ORDER_PAYMENT_INITIATE, new ArrayList<OrderStateEnum>() {
            {
                add(ORDER_PAYMENT_SUCCESS);
                add(ORDER_PAYMENT_FAILED);
            }
        });


        stateMovementList.put(ORDER_PAYMENT_SUCCESS, new ArrayList<OrderStateEnum>() {
            {
                add(ORDER_TOURNAMENT_JOIN_INITIATE);
            }
        });

        stateMovementList.put(ORDER_TOURNAMENT_JOIN_INITIATE, new ArrayList<OrderStateEnum>() {
            {
                add(ORDER_TOURNAMNET_JOINED);
                add(ORDER_FAILED);
            }
        });


        stateMovementList.put(ORDER_TOURNAMNET_JOINED, new ArrayList<OrderStateEnum>() {
            {
                add(ORDER_TOURNAMENT_START);
            }
        });

        stateMovementList.put(ORDER_TOURNAMENT_START, new ArrayList<OrderStateEnum>() {
            {
                add(ORDER_TOURNAMENT_END);
                add(ORDER_FAILED);
            }
        });

        stateMovementList.put(ORDER_TOURNAMENT_END, new ArrayList<OrderStateEnum>() {
            {
                add(ORDER_PRIZE_PROCESSED);
                add(ORDER_FAILED);
            }
        });

        stateMovementList.put(ORDER_PRIZE_PROCESSED, new ArrayList<OrderStateEnum>() {
            {
                add(ORDER_SUCCESS);
                add(ORDER_CREDIT_FAILED);
            }
        });

        orderStateMap = Arrays.stream(OrderStateEnum.values()).collect(Collectors.toMap(OrderStateEnum::getOrderState, Function.identity()));

    }

    /**
     * Is valid state transition boolean.
     *
     * @param currentState the order state enum
     * @param updatedState the order state enum
     * @return the boolean
     */
    public static boolean isValidStateTransition(OrderStateEnum currentState, OrderStateEnum updatedState) {

        if (stateMovementList.get(currentState).contains(updatedState)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
