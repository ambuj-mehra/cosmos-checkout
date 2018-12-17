package com.cosmos.checkout.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
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
            "paytmlogo url", "gKpu7IKaLSbkchFS", "rxazcv89315285244163"),

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

    static {
        paymentModes = Arrays.stream(PaymentMode.values()).collect(Collectors.toList());
    }
}
