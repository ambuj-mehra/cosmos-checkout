package com.cosmos.service.impl;

import com.cosmos.checkout.dto.InitiatePaymentRequestDto;
import com.cosmos.checkout.dto.PaymentResponseDto;
import com.cosmos.checkout.enums.PaymentMode;
import com.cosmos.exception.CheckoutException;
import com.cosmos.service.IPaymentDetailsService;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

@Service
public class PaytmPaymentsService implements IPaymentDetailsService {
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
            parameterMap.put("CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + initiatePaymentRequestDto.getTransactionId());
            String paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(PaymentMode.PAYTM.getPaymentModeSecretkey(), parameterMap);
            parameterMap.put("CHECKSUM", paytmChecksum);
            paymentOptionData = PaymentResponseDto.PaymentOptionData.builder()
                    .paymentModeId(initiatePaymentRequestDto.getPaymentModeId())
                    .cosmosMerchantId(PaymentMode.PAYTM.getCosmosMerchantId())
                    .cosmosSecretKey(PaymentMode.PAYTM.getPaymentModeSecretkey())
                    .postingUrl(PaymentMode.PAYTM.getPaymentUrl())
                    .parameterMap(parameterMap)
                    .build();
        } catch (Exception exception) {
            throw new CheckoutException("Paytm param generation failed");
        }
        return paymentOptionData;
    }
}
