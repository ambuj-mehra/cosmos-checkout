package com.cosmos.repository;

import com.cosmos.entity.Orders;
import org.springframework.data.repository.CrudRepository;

/**
 * The interface Orders repository.
 *
 * @author ambujmehra
 */
public interface OrdersRepository extends CrudRepository<Orders, Long> {

    Orders findByTransactionId(String transactionId);
}
