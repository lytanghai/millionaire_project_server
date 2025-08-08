package com.millionaire_project.millionaire_project.constant;

import java.math.BigDecimal;
import java.time.ZoneId;

public class Static {


    public static final String CREDENTIAL_SEQ = "expense_tracker_id_seq";

    //COMMON
    public static final String COMMA = ",";
    public static final String EMPTY = "";
    public static final String PERIOD = ".";
    public static final String USD = "USD";
    public static final String KHR = "KHR";
    public static final String DAY = "DAY";

    public static final BigDecimal USD_TO_KHR_RATE = new BigDecimal("4000");

    public static final ZoneId PHNOM_PENH = ZoneId.of("Asia/Phnom_Penh");
}
