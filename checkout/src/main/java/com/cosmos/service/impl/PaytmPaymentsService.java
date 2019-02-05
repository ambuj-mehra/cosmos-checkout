package com.cosmos.service.impl;

import com.cosmos.checkout.dto.InitiatePaymentRequestDto;
import com.cosmos.checkout.dto.PaymentResponseDto;
import com.cosmos.checkout.dto.PaytmOrderStatusRequestDto;
import com.cosmos.checkout.dto.PaytmOrderStatusResponseDto;
import com.cosmos.checkout.enums.PaymentMode;
import com.cosmos.client.PaytmClient;
import com.cosmos.exception.CheckoutException;
import com.cosmos.service.IPaymentDetailsService;
import com.cosmos.utils.CheckoutUtils;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class PaytmPaymentsService implements IPaymentDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaytmPaymentsService.class);

    @Autowired
    private PaytmClient paytmClient;

    @Autowired
    private CheckoutUtils checkoutUtils;

    @Override
    public PaymentResponseDto.PaymentOptionData getPaymentsOptionData(InitiatePaymentRequestDto initiatePaymentRequestDto) {
        PaymentResponseDto.PaymentOptionData paymentOptionData = null;
        try {
            TreeMap<String, String> parameterMap = new TreeMap<>();
            parameterMap.put("MID", PaymentMode.PAYTM.getCosmosMerchantId());
            parameterMap.put("ORDER_ID", initiatePaymentRequestDto.getTransactionId());
            parameterMap.put("CHANNEL_ID", "WAP");
            parameterMap.put("CUST_ID", initiatePaymentRequestDto.getUserCode());
            parameterMap.put("TXN_AMOUNT", initiatePaymentRequestDto.getTotalOrderAmount().toString());
            parameterMap.put("WEBSITE", "WEBSTAGING");
            parameterMap.put("INDUSTRY_TYPE_ID", "Retail");
            parameterMap.put("CALLBACK_URL", "https://753c9cd7.ngrok.io/payment/paytm/callback?ORDER_ID=" +
                    initiatePaymentRequestDto.getTransactionId());
            String paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(
                    PaymentMode.PAYTM.getPaymentModeSecretkey(), parameterMap);
            parameterMap.put("CHECKSUM", paytmChecksum);
            paymentOptionData = PaymentResponseDto.PaymentOptionData.builder()
                    .paymentModeId(initiatePaymentRequestDto.getPaymentModeId())
                    .cosmosMerchantId(PaymentMode.PAYTM.getCosmosMerchantId())
                    .postingUrl(PaymentMode.PAYTM.getPaymentUrl())
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
                    PaymentMode.PAYTM.getPaymentModeSecretkey(),
                    paytmParams, paytmChecksum);
        } catch (Exception exception) {
            throw new CheckoutException("error in calculating checksum :: {}", exception);
        }
        if(!isValidChecksum)
            throw new CheckoutException("Paytm checksum Mismatch on callback");
    }

    @Override
    public boolean checkOrderstatusFromPaymentMode(Map<String, String> paymentsCallbackParams) {
        TreeMap<String, String> paytmParams = new TreeMap<>();
        paytmParams.put("MID", PaymentMode.PAYTM.getCosmosMerchantId());
        paytmParams.put("ORDERID", paymentsCallbackParams.get("ORDERID"));
        try {
            String paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(
                    PaymentMode.PAYTM.getPaymentModeSecretkey(), paytmParams);
            PaytmOrderStatusRequestDto paytmOrderStatusRequestDto = PaytmOrderStatusRequestDto.builder()
                    .orderId(paymentsCallbackParams.get("ORDERID"))
                    .mid(PaymentMode.PAYTM.getCosmosMerchantId())
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
