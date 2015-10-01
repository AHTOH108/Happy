package com.iandp.happy.utils;

import android.content.Context;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Created on 20.08.2015.
 */
public class DateUtil {

    public static final long MIN_DATE = -2208988800000L;

    private static SimpleDateFormat sdf;

    public static String getDateLongToString(long millisecond) {
        sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        return sdf.format(millisecond);
    }

    public static String getDateShortToString(long millisecond) {
        sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        return sdf.format(millisecond);
    }

    public static String translateToStringDate(long millisecond, String format) {
        sdf = new SimpleDateFormat(format, Locale.getDefault());

        return sdf.format(millisecond);
    }

    public static long translateToLongDate(String stringDate, String format) {
        if (TextUtils.isEmpty(stringDate))
            return Calendar.getInstance().getTimeInMillis();

        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date();
        try {
            date = formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getDateEventTizer(int timeFrom, int timeTo) {

        String stringDate;

        long dateFrom = timeFrom * 1000;
        long dateTo = timeTo * 1000;

        if ((dateFrom <= 0) && (dateTo <= 0)) {
            dateFrom = dateTo = System.currentTimeMillis();
        } else if ((dateFrom > 0) && (dateTo <= 0)) {
            dateTo = dateFrom;
        } else if ((dateFrom <= 0) && (dateTo > 0)) {
            dateFrom = dateTo;
        }

        Calendar calendarFrom = GregorianCalendar.getInstance(Locale.getDefault());
        Calendar calendarTo = GregorianCalendar.getInstance(Locale.getDefault());

        calendarFrom.setTimeInMillis(dateFrom);
        calendarTo.setTimeInMillis(dateTo);

        if (Math.abs(calendarTo.get(Calendar.MONTH) - calendarFrom.get(Calendar.MONTH)) != 0) {

            sdf = new SimpleDateFormat("dd MM", Locale.getDefault());
            stringDate = sdf.format(dateFrom) + " - " + sdf.format(dateTo);
        } else if (Math.abs(calendarTo.get(Calendar.DAY_OF_MONTH) - calendarFrom.get(Calendar.DAY_OF_MONTH)) != 0) {

            sdf = new SimpleDateFormat("MMMM", Locale.getDefault());
            stringDate = calendarFrom.get(Calendar.DAY_OF_MONTH) + " - " + calendarTo.get(Calendar.DAY_OF_MONTH) + sdf.format(dateFrom);
        } else {
            sdf = new SimpleDateFormat("dd MMMM", Locale.getDefault());
            stringDate = sdf.format(dateFrom);
        }

        return stringDate;
    }

    public static String getDateEventFull(int timeFrom, int timeTo, String dayTimeFrom, String dayTimeTo, Context context) {

        if (dayTimeFrom == null) {
            dayTimeFrom = "";
        }

        if (dayTimeTo == null) {

                    dayTimeTo = "";
        }
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getDateEventTizer(timeFrom, timeTo));

        if ((dayTimeFrom.length() > 0) || (dayTimeTo.length() > 0)) {
            stringBuilder.append(",");

/*            if (dayTimeFrom.length() > 0) {
                stringBuilder.append(" ").append(context.getResources().getString(R.string.string_from_date)).append(" ").append(dayTimeFrom);
            }

            if (dayTimeTo.length() > 0) {
                stringBuilder.append(" ").append(context.getResources().getString(R.string.string_to_date)).append(" ").append(dayTimeTo);
            }*/
        }

        return stringBuilder.toString();
    }
}
