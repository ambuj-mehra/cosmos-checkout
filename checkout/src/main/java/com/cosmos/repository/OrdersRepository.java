package com.cosmos.repository;

import com.cosmos.entity.Orders;
import org.springframework.data.repository.CrudRepository;

/**
 * The interface Orders repository.
 *
 * @author ambujmehra
 */
public interface OrdersRepository extends CrudRepository<Orders, Long> {

    /**
     * Find by transaction id orders.
     *
     * @param transactionId the transaction id
     * @return the orders
     */
    Orders findByTransactionId(String transactionId);

    /**
     * Find by tournament code and user code orders.
     *
     * @param tournamentCode the tournament code
     * @param userCode       the user code
     * @return the orders
     */
    Orders findByTournamentCodeAndUserCode(String tournamentCode, String userCode);

    /**
     * Find by tournament code and user code and order status orders.
     *
     * @param tournamentCode the tournament code
     * @param userCode       the user code
     * @param orderStatus    the order status
     * @return the orders
     */
    Orders findByTournamentCodeAndUserCodeAndOrderStatus(String tournamentCode, String userCode, Integer orderStatus);
}
