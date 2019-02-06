package com.cosmos.repository;

import com.cosmos.entity.TransactionLedger;
import org.springframework.data.repository.CrudRepository;

/**
 * The interface Transaction ledger repository.
 *
 * @author ambujmehra
 */
public interface TransactionLedgerRepository extends CrudRepository<TransactionLedger, Long> {
}
