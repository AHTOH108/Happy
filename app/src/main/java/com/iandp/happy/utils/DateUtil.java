package com.iandp.happy.utils;

import org.joda.time.DateTime;

/**
 * Created on 20.08.2015.
 */
public class DateUtil {

    public static final long MIN_DATE = -2208988800000L;

    public static long getNowTime() {
        DateTime dateTime = DateTime.now();

        return dateTime.getMillis();
    }

    public static String getStringDateForMask(long dateTimeFull, String dataFormat) {
        String dateText;
        DateTime dateTime = new DateTime(dateTimeFull);
        dateText = dateTime.toString(dataFormat);
        if (dateText != null)
            return dateText;
        else
            return "";
    }

}
