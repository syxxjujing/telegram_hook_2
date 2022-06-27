package com.jujing.telehook_2.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2019/4/10.
 */

public class DateUtils {


    public static String formatDate(long timeStamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(timeStamp);

    }

    public static long getCurStringTime() {
        
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            String nowTime = simpleDateFormat.format(date);

            Date parse = simpleDateFormat.parse(nowTime);
            return parse.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 判断是否在时间区间
     */
    public static boolean isInTime(String startH, String endH, String startMinute, String endMinute, long compareTime) {
        long startTime = getStringTime(startH + ":" + startMinute);
        long endTime = getStringTime(endH + ":" + endMinute);

//        long curStringTime = getCurStringTime();
        if (compareTime >= startTime && compareTime <= endTime) {
            return true;
        }
        return false;

    }

    public static long getStringTime(String time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date date = simpleDateFormat.parse(time);

            return date.getTime();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }


    public static int getHour(long timeStamp) {
        Calendar instance = Calendar.getInstance();

        Date date = new Date();
        instance.setTime(date);
        return instance.get(Calendar.HOUR_OF_DAY);
    }


    public static int getDay(long timeStamp) {
        Calendar instance = Calendar.getInstance();
        Date date = new Date(timeStamp);
        instance.setTime(date);
        return instance.get(Calendar.DAY_OF_MONTH);
    }

}
