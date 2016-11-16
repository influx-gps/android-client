package com.gut.follower.commons;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateConverter {

    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String DATE_FORMAT_TIME = "dd-MM-yyyy HH:mm";

    public static String convertDate(long time){
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.format(date);
    }

    public static String convertDateWithTime(long time){
        return convertDateWithTime(time, null);
    }

    public static String convertDateWithTime(long time, TimeZone timeZone){
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_TIME);
        if(timeZone != null)
            simpleDateFormat.setTimeZone(timeZone);
        else
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(date);
    }


    public static String convertToTime(long startTime, long endTime){
        long time = (endTime - startTime)/1000;
        long hours = time / 3600;
        long minutes = (time % 3600) / 60;
        long seconds = (time - hours*3600 - minutes*60);
        return String.format("%d:%d:%d", hours, minutes, seconds);
    }
}
