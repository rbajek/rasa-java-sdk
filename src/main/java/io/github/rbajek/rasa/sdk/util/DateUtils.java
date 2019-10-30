package io.github.rbajek.rasa.sdk.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter ISO_8601_WITH_OUT_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDateTime convertToUtc(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public static String toISO8601Format(LocalDateTime time) {
        return ISO_8601_FORMATTER.format(time);
    }

    public static String toISO8601Format(LocalDate date) {
        return ISO_8601_WITH_OUT_TIME_FORMATTER.format(date);
    }
}
