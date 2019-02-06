package com.cosmos.repository;

import com.cosmos.entity.TransactionLedger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * The interface Transaction ledger repository.
 *
 * @author ambujmehra
 */
public interface TransactionLedgerRepository extends PagingAndSortingRepository<TransactionLedger, Long> {

    List<TransactionLedger> findAllByUserCode(String userCode, Pageable pageable);
}
