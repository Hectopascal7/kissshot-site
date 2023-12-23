package cn.com.kissshot.util.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class KissshotDateUtil {

    public static String convertDateToString(Date date, String format) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return dateTimeFormatter.format(localDateTime);
    }

}
