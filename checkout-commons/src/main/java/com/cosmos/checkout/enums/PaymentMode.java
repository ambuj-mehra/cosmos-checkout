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
    PAYTM(0, "https://securegw-stage.paytm.in/theia/processTransaction",
            "paytmlogo url", "aT2HMtSE!T9ovb9f", "fwLjWr93089220547348"),

    /**
     * The Mobikwik.
     */
    MOBIKWIK(1, "https://securegw-stage.paytm.in/theia/processTransaction",
            "mobikwik url", "gKpu7IKaLSbkchFS", "rxazcv89315285244163");


    private Integer paymentModeId;
    private String paymentUrl;
    private String paymentModeLogo;
    private String paymentModeSecretkey;
    private String cosmosMerchantId;

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
