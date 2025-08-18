package com.millionaire_project.millionaire_project.util;

import com.millionaire_project.millionaire_project.constant.Static;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <dependency>
 *   <groupId>org.apache.commons</groupId>
 *   <artifactId>commons-lang3</artifactId>
 *   <version>3.8.1</version>
 * </dependency>
 * */

public final class DateUtil extends DateUtils {

    public static final String DATE_WITH_TIME_1 = "dd-MM-yyyy HH:mm:ss";
    public static final String DATE_WITH_TIME_2 = "dd-MMM-yyyy";

    private static final DateTimeFormatter PG_TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss HH:mm:ss");

    public static String[] getCurrentYearMonth() {
        LocalDate today = LocalDate.now();
        String year = today.format(DateTimeFormatter.ofPattern("yyyy"));
        String month = today.format(DateTimeFormatter.ofPattern("MM"));
        return new String[]{year, month};
    }


    public static String convertUnixToDt(long unixTimestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(unixTimestamp),
                Static.PHNOM_PENH
        );

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_WITH_TIME_1);

        return dateTime.format(formatter);

    }
    public static boolean isTodayFirstDayOfMonth() {
        LocalDate today = LocalDate.now();
        return today.getDayOfMonth() == 1;
    }

    public static boolean refreshBalance(Date refreshDate) {
        Date now = new Date();

        // Check if now is after or equal to refreshDate
        return !now.before(refreshDate);
    }

    public static Date convertToPhnomPenhDate(Date inputDate) {
        if (inputDate == null) return null;

        Instant instant = inputDate.toInstant();
        ZonedDateTime zdt = instant.atZone(Static.PHNOM_PENH);

        // Convert ZonedDateTime back to Instant (same moment in time)
        Instant zonedInstant = zdt.toInstant();

        // Return new Date with same instant millis (no actual time shift)
        return Date.from(zonedInstant);
    }

    public static Date getFirstDateOfNextMonthUtilDate() {
        LocalDate firstDayNextMonth = LocalDate.now(Static.PHNOM_PENH)
                .plusMonths(1)
                .withDayOfMonth(1);
        return Date.from(firstDayNextMonth.atStartOfDay(Static.PHNOM_PENH).toInstant());
    }
    public static Date getNextDateAtMidnight() {
        LocalDate tomorrow = LocalDate.now(Static.PHNOM_PENH).plusDays(1);
        return Date.from(tomorrow.atStartOfDay(Static.PHNOM_PENH).toInstant());
    }

    public static String format(Date date) {
        return format(date, DATE_WITH_TIME_1);
    }

    public static String format(Date date, String format) {
        return format(date, format, null);
    }

    public static String format(Date date, String format, String defaultValue) {
        return date == null ? defaultValue : new SimpleDateFormat(format).format(date);
    }



}