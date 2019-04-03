package com.cosmos.checkout.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The enum Payment mode.
 *
 * @author ambujmehra
 */
@AllArgsConstructor
@Getter
public enum PaymentMode {

    /**
     * The Paytm payment mode
     */
    PAYTM(0, "https://securegw.paytm.in/theia/processTransaction",
            "paytmlogo url", "7KiP62X1#IO2HwEJ", "SpeHqH11050895314462", true),

    COSMOS_CASH(1, null, "cosmos_cash logo", null, null, false);

    private Integer paymentModeId;
    private String paymentUrl;
    private String paymentModeLogo;
    private String paymentModeSecretkey;
    private String cosmosMerchantId;
    private boolean isExternalMode;

    /**
     * The Payment modes.
     */
    public static List<PaymentMode> paymentModes;

    /**
     * The Payment mode map.
     */
    public static Map<Integer, PaymentMode> paymentModeMap;

    static {
        paymentModes = Arrays.stream(PaymentMode.values())
                .collect(Collectors.toList());

        paymentModeMap = Arrays.stream(PaymentMode.values())
                .collect(Collectors.toMap(PaymentMode::getPaymentModeId, Function.identity()));
    }

    /**
     * Gets payment mode by id.
     *
     * @param paymentModeId the payment mode id
     * @return the payment mode by id
     */
    public static PaymentMode getPaymentModeById(Integer paymentModeId) {
        return paymentModeMap.get(paymentModeId);
    }
}
