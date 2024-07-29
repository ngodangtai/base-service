package com.company.module.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    public static final String FORMAT_SQLSERVER_SHORT = "yyyy-MM-dd";
    public static final String FULL_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String SHORT_TIME_PATTERN = "yyyy-MM-dd";
    public static final String FORMAT_SQLSERVER_NEW = "dd-MM-yyyy";
    public static final String SHORT_DATE_VN_PATTERN = "dd/MM/yyyy";
    public static final String DEFAULT_TIMEZONE_GMT7 = "GMT+7";

    public static String format(Date d, String format) {
        if (d == null) {
            return null;
        }
        if (StringUtils.isEmpty(format)) {
            format = FORMAT_SQLSERVER_SHORT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    public static Date convertStartOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date = cal.getTime();
        return date;
    }

    public static Date getShiftDate(Date date, int i) {
        if (i == 0) {
            return date;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);
        date = c.getTime();
        return date;
    }

}
