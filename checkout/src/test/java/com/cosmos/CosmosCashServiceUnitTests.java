package com.cosmos;

import com.cosmos.checkout.dto.CosmosCashDto;
import com.cosmos.entity.UserCosmosCash;
import com.cosmos.repository.UserCosmosCashRepository;
import com.cosmos.service.ICosmosCashService;
import com.cosmos.service.impl.CosmosCashServiceImpl;
import com.cosmos.service.impl.OrderDetailsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CosmosCashServiceUnitTests {

    @TestConfiguration
    static class UserCosmosCashServiceTestContextConfiguration {
        @Bean
        public CosmosCashServiceImpl cosmosCashServiceImple() {
            return new CosmosCashServiceImpl();
        }
    }

    @Autowired
    private ICosmosCashService cosmosCashService;

    @MockBean
    private UserCosmosCashRepository userCosmosCashRepository;

    @MockBean
    private OrderDetailsService orderDetailsService;

    @Before
    public void setUp() {
        UserCosmosCash userCosmosCash = new UserCosmosCash();
        userCosmosCash.setUserCode("1234");
        userCosmosCash.setCosmosCash(BigDecimal.TEN);

        UserCosmosCash zeroUserCosmosCash = new UserCosmosCash();
        zeroUserCosmosCash.setUserCode("12345");
        zeroUserCosmosCash.setCosmosCash(BigDecimal.ZERO);

        Mockito.when(userCosmosCashRepository.findByUserCode(userCosmosCash.getUserCode()))
                .thenReturn(userCosmosCash);

        Mockito.when(userCosmosCashRepository.findByUserCode(zeroUserCosmosCash.getUserCode()))
                .thenReturn(zeroUserCosmosCash);

        Mockito.when(userCosmosCashRepository.save(Mockito.any(UserCosmosCash.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void testGetUserCosmosCashBalance() {
        String userCode = "1234";
        CosmosCashDto result = cosmosCashService.getUserCosmosCashBalance(userCode);
        CosmosCashDto expectedDto = CosmosCashDto.builder()
                .userCode(userCode)
                .cosmosCash(BigDecimal.TEN)
                .build();
        Assert.assertEquals(result.getCosmosCash(), expectedDto.getCosmosCash());
    }

    @Test
    public void testZeroCosmosCashBalance() {
        String userCode = "12345";
        CosmosCashDto result = cosmosCashService.getUserCosmosCashBalance(userCode);
        CosmosCashDto expectedDto = CosmosCashDto.builder()
                .userCode(userCode)
                .cosmosCash(BigDecimal.ZERO)
                .build();
        Assert.assertEquals(result.getCosmosCash(), expectedDto.getCosmosCash());

    }

    @Test
    public void testCreditCosmosCash() {

        String userCode = "1234";
        CosmosCashDto expectedDto = CosmosCashDto.builder()
                .userCode(userCode)
                .cosmosCash(BigDecimal.valueOf(20))
                .build();
        CosmosCashDto result = cosmosCashService.creditCosmosCash(userCode, BigDecimal.TEN, true);
        Assert.assertEquals(result.getCosmosCash(), expectedDto.getCosmosCash());
    }

    @Test
    public void testDebitCosmosCash() {

        String userCode = "1234";
        CosmosCashDto expectedDto = CosmosCashDto.builder()
                .userCode(userCode)
                .cosmosCash(BigDecimal.ZERO)
                .build();
        CosmosCashDto result = cosmosCashService.debitCosmosCash(userCode, BigDecimal.TEN, true);
        Assert.assertEquals(result.getCosmosCash(), expectedDto.getCosmosCash());
    }


}
