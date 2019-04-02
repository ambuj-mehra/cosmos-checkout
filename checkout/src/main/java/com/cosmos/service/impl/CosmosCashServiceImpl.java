package com.cosmos.service.impl;

import com.cosmos.checkout.dto.CosmosCashDto;
import com.cosmos.entity.UserCosmosCash;
import com.cosmos.exception.CheckoutException;
import com.cosmos.repository.UserCosmosCashRepository;
import com.cosmos.service.ICosmosCashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * The type Cosmos cash service.
 *
 * @author ambujmehra
 */
@Service
public class CosmosCashServiceImpl implements ICosmosCashService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CosmosCashServiceImpl.class);

    @Autowired
    private UserCosmosCashRepository userCosmosCashRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CosmosCashDto getUserCosmosCashBalance(String userCode) {
        LOGGER.info("Getting cosmos balance for user :: {}", userCode);
        UserCosmosCash userCosmosCash = userCosmosCashRepository.findByUserCode(userCode);
        Optional.ofNullable(userCosmosCash).orElseThrow(() -> new CheckoutException("Cosmos cash not found"));
        LOGGER.info("Cosmos cash available for user ::{} is  :: {}", userCode, userCosmosCash.getCosmosCash());
        return CosmosCashDto.builder()
                .cosmosCash(userCosmosCash.getCosmosCash())
                .userCode(userCosmosCash.getUserCode())
                .build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CosmosCashDto creditCosmosCash(String userCode, BigDecimal creditCosmosCash) {
        LOGGER.info("Credit cosmos balance for user :: {} with amount :: {}", userCode, creditCosmosCash);
        UserCosmosCash userCosmosCash = userCosmosCashRepository.findByUserCode(userCode);
        Optional.ofNullable(userCosmosCash).orElseThrow(() -> new CheckoutException("Cosmos cash not found"));
        BigDecimal updatedCosmosCash = userCosmosCash.getCosmosCash().add(creditCosmosCash);
        userCosmosCash.setCosmosCash(updatedCosmosCash);
        userCosmosCash =  userCosmosCashRepository.save(userCosmosCash);
        return CosmosCashDto.builder()
                .cosmosCash(userCosmosCash.getCosmosCash())
                .userCode(userCosmosCash.getUserCode())
                .build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CosmosCashDto debitCosmosCash(String userCode, BigDecimal debitCosmosCash) {
        LOGGER.info("Credit cosmos balance for user :: {} with amount :: {}", userCode, debitCosmosCash);
        UserCosmosCash userCosmosCash = userCosmosCashRepository.findByUserCode(userCode);
        Optional.ofNullable(userCosmosCash).orElseThrow(() -> new CheckoutException("Cosmos cash not found"));
        BigDecimal updatedCosmosCash = userCosmosCash.getCosmosCash().subtract(debitCosmosCash);
        userCosmosCash.setCosmosCash(updatedCosmosCash);
        userCosmosCash = userCosmosCashRepository.save(userCosmosCash);
        return CosmosCashDto.builder()
                .cosmosCash(userCosmosCash.getCosmosCash())
                .userCode(userCosmosCash.getUserCode())
                .build();
    }

    @Override
    public CosmosCashDto createCosmosWalletForUser(String userCode, BigDecimal initialCosmosBalance) {
        LOGGER.info("Create cosmos balance wallet for user :: {} with initial amount :: {}", userCode, initialCosmosBalance);
        UserCosmosCash currentUserCosmosCash = userCosmosCashRepository.findByUserCode(userCode);
        if (currentUserCosmosCash != null) {
            LOGGER.info("Wallet already created for user :: {}", userCode);
            return CosmosCashDto.builder()
                    .cosmosCash(currentUserCosmosCash.getCosmosCash())
                    .userCode(currentUserCosmosCash.getUserCode())
                    .build();
        } else {
            UserCosmosCash userCosmosCash = new UserCosmosCash();
            userCosmosCash.setCosmosCash(initialCosmosBalance);
            userCosmosCash.setUserCode(userCode);
            userCosmosCash = userCosmosCashRepository.save(userCosmosCash);
            return CosmosCashDto.builder()
                    .cosmosCash(userCosmosCash.getCosmosCash())
                    .userCode(userCosmosCash.getUserCode())
                    .build();
        }
    }

}
