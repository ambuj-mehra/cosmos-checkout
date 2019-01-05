package com.cosmos.client;

import com.cosmos.checkout.dto.PaytmOrderStatusRequestDto;
import com.cosmos.checkout.dto.PaytmOrderStatusResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The type Paytm client.
 *
 * @author ambujmehra
 */
@Component
@FeignClient(name = "paytm-client", url = "${paytm.feign.url}")
public interface PaytmClient {

    @RequestMapping(value = "merchant-status/getTxnStatus", method = RequestMethod.POST, consumes = "application/json")
    PaytmOrderStatusResponseDto getPaytmTransactionStatus(@RequestBody PaytmOrderStatusRequestDto paytmOrderStatusRequestDto);
}
