package utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a");

    public static String    formatDate(LocalDate d) { return d != null ? d.format(DATE_FMT) : "—"; }
    public static String    formatTime(LocalTime t) { return t != null ? t.format(TIME_FMT) : "—"; }
    public static LocalDate today()                 { return LocalDate.now(); }
    public static LocalTime now()                   { return LocalTime.now(); }
}