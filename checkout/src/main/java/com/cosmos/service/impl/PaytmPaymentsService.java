package com.cosmos.service.impl;

import com.cosmos.checkout.dto.InitiatePaymentRequestDto;
import com.cosmos.checkout.dto.PaymentResponseDto;
import com.cosmos.checkout.dto.PaytmOrderStatusRequestDto;
import com.cosmos.checkout.dto.PaytmOrderStatusResponseDto;
import com.cosmos.client.PaytmClient;
import com.cosmos.exception.CheckoutException;
import com.cosmos.service.IPaymentDetailsService;
import com.cosmos.utils.CheckoutUtils;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class PaytmPaymentsService implements IPaymentDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaytmPaymentsService.class);

    @Value("${paytm.website}")
    private String paytmWebsite;

    @Value("${paytm.callback.base.url}")
    private String paytmCallbackBaseUrl;

    @Value("${paytm.mid}")
    private String paytmMid;

    @Value("${paytm.mid.secretkey}")
    private String paytmMidSecretKey;

    @Value("${paytm.feign.url}")
    private String paymentGatewayUrl;

    @Autowired
    private PaytmClient paytmClient;

    @Autowired
    private CheckoutUtils checkoutUtils;

    @Override
    public PaymentResponseDto.PaymentOptionData getPaymentsOptionData(InitiatePaymentRequestDto initiatePaymentRequestDto) {
        PaymentResponseDto.PaymentOptionData paymentOptionData;
        try {
            TreeMap<String, String> parameterMap = new TreeMap<>();
            parameterMap.put("MID", paytmMid);
            parameterMap.put("ORDER_ID", initiatePaymentRequestDto.getTransactionId());
            parameterMap.put("CHANNEL_ID", "WAP");
            parameterMap.put("CUST_ID", initiatePaymentRequestDto.getUserCode());
            parameterMap.put("TXN_AMOUNT", initiatePaymentRequestDto.getActualOrderAmount().toString());
            parameterMap.put("WEBSITE", paytmWebsite);
            parameterMap.put("INDUSTRY_TYPE_ID", "Retail");
            parameterMap.put("CALLBACK_URL", paytmCallbackBaseUrl + "/payment/paytm/callback?ORDER_ID=" +
                    initiatePaymentRequestDto.getTransactionId());
            String paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(
                    paytmMidSecretKey, parameterMap);
            parameterMap.put("CHECKSUM", paytmChecksum);
            paymentOptionData = PaymentResponseDto.PaymentOptionData.builder()
                    .paymentModeId(initiatePaymentRequestDto.getPaymentModeId())
                    .cosmosMerchantId(paytmMid)
                    .postingUrl(paymentGatewayUrl)
                    .parameterMap(parameterMap)
                    .build();
        } catch (Exception exception) {
            throw new CheckoutException("Paytm param generation failed");
        }
        return paymentOptionData;
    }

    @Override
    public String getCosmosTransactionIdFromCallbackParams(Map<String, String> paymentsCallbackParams) {
        return paymentsCallbackParams.get("ORDERID");
    }

    @Override
    public String getPaymentModeTransactionId(Map<String, String> paymentsCallbackParams) {
        return paymentsCallbackParams.get("TXNID");
    }

    @Override
    public String getPaymentResponseMessage(Map<String, String> paymentsCallbackParams) {
        return paymentsCallbackParams.get("RESPMSG");
    }

    @Override
    public void validateCallbackChecksum(Map<String, String> paymentsCallbackParams) {
        TreeMap<String, String> paytmParams = new TreeMap<>();
        String paytmChecksum = null;
        boolean isValidChecksum = false;
        for (Map.Entry<String, String> requestParamsEntry : paymentsCallbackParams.entrySet()) {
            if ("CHECKSUMHASH".equalsIgnoreCase(requestParamsEntry.getKey()))
                paytmChecksum = requestParamsEntry.getValue();
            else
                paytmParams.put(requestParamsEntry.getKey(), requestParamsEntry.getValue());
        }
        try {
            isValidChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(
                    paytmMidSecretKey, paytmParams, paytmChecksum);
        } catch (Exception exception) {
            throw new CheckoutException("error in calculating checksum :: {}", exception);
        }
        if(!isValidChecksum)
            throw new CheckoutException("Paytm checksum Mismatch on callback");
    }

    @Override
    public boolean checkOrderstatusFromPaymentMode(Map<String, String> paymentsCallbackParams) {
        TreeMap<String, String> paytmParams = new TreeMap<>();
        paytmParams.put("MID", paytmMid);
        paytmParams.put("ORDERID", paymentsCallbackParams.get("ORDERID"));
        try {
            String paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(
                    paytmMidSecretKey, paytmParams);
            PaytmOrderStatusRequestDto paytmOrderStatusRequestDto = PaytmOrderStatusRequestDto.builder()
                    .orderId(paymentsCallbackParams.get("ORDERID"))
                    .mid(paytmMid)
                    .checksum(paytmChecksum)
                    .build();
            PaytmOrderStatusResponseDto paytmOrderStatusResponseDto = paytmClient.getPaytmTransactionStatus(
                    paytmOrderStatusRequestDto);
            LOGGER.info("received response from paytm for tranasaction status is :: {}",
                    paytmOrderStatusResponseDto.toString());
            return checkoutUtils.isPaytmTransactionSuccess(paymentsCallbackParams, paytmOrderStatusResponseDto);
        } catch (Exception exception) {
            LOGGER.error("exception in validating paytm callback :: {}", exception);
        }
        return false;
    }

}
