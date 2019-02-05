package com.cosmos.repository;

import com.cosmos.entity.OrderPayment;
import org.springframework.data.repository.CrudRepository;

/**
 * The interface Order payment repository.
 *
 * @author ambujmehra
 */
public interface OrderPaymentRepository extends CrudRepository<OrderPayment, Long> {
}
