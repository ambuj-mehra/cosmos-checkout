package com.cosmos.repository;

import com.cosmos.entity.UserCosmosCash;
import org.springframework.data.repository.CrudRepository;

/**
 * The interface User cosmos cash repository.
 *
 * @author ambujmehra
 */
public interface UserCosmosCashRepository extends CrudRepository<UserCosmosCash, Long> {

    /**
     * Find by user code user cosmos cash.
     *
     * @param userCode the user code
     * @return the user cosmos cash
     */
    UserCosmosCash findByUserCode(String userCode);
}
