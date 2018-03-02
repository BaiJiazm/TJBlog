package com.baijiazm.tjblog.utils;

import java.util.Date;

public class DateKit {

    public static int getCurrentUnixTime() {
        return getUnixTimeByDate(new Date());
    }

    public static int getUnixTimeByDate(Date date) {
        return (int) (date.getTime() / 1000L);
    }
}
