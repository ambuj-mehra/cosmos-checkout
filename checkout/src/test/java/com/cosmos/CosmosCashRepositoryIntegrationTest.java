package com.cosmos;

import com.cosmos.entity.UserCosmosCash;
import com.cosmos.repository.UserCosmosCashRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CosmosCashRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserCosmosCashRepository userCosmosCashRepository;

    @Test
    public void checkUserCosmosCashPersistence() {
        UserCosmosCash userCosmosCash = new UserCosmosCash();
        userCosmosCash.setUserCode("1234");
        userCosmosCash.setCosmosCash(BigDecimal.TEN);

        testEntityManager.persist(userCosmosCash);
        testEntityManager.flush();

        UserCosmosCash getUserCosmosCash = userCosmosCashRepository.findByUserCode("1234");
        Assert.assertEquals(getUserCosmosCash.getCosmosCash(), userCosmosCash.getCosmosCash());
    }


}
