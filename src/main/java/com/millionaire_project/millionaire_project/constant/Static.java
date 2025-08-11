package com.millionaire_project.millionaire_project.constant;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class Static {


    public static final String CREDENTIAL_SEQ = "credential_seq";

    //COMMON
    public static final String COMMA = ",";
    public static final String EMPTY = "";
    public static final String PERIOD = ".";
    public static final String USD = "USD";
    public static final String KHR = "KHR";
    public static final String DAY = "DAY";

    public static final BigDecimal USD_TO_KHR_RATE = new BigDecimal("4000");

    public static final ZoneId PHNOM_PENH = ZoneId.of("Asia/Phnom_Penh");


    public static final String CRYPTO_PANIC_BASE_URL = "https://cryptopanic.com/api/developer/v2/posts/";

    public static final String CURRENCIES = "currencies";
    public static final String AUTH_TOKEN = "auth_token";

    public static final Map<String,String> URL_MAPPING = (Map<String, String>) new HashMap<>()
            .put("CRYPTO_PANIC", CRYPTO_PANIC_BASE_URL);
}
