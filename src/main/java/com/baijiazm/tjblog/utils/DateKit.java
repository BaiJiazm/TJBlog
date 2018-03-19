package com.baijiazm.tjblog.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateKit {

    public static int getCurrentUnixTime() {
        return getUnixTimeByDate(new Date());
    }

    public static int getUnixTimeByDate(Date date) {
        return (int) (date.getTime() / 1000L);
    }

    public static String formatDateByUnixTime(long unixTime, String dateFormat) {
        return dateFormat(new Date(unixTime * 1000L), dateFormat);
    }

    public static String dateFormat(Date date, String dateFormat) {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            return format.format(date);
        }

        return "";
    }


}
