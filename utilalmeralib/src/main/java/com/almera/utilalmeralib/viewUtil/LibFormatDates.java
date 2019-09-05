package com.almera.utilalmeralib.viewUtil;

import java.util.Calendar;

public class LibFormatDates {
    public static String formatTo12 (int hour,int minutes){
        String am_pm = "";
        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hour);
        datetime.set(Calendar.MINUTE, minutes);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
        String minutos = (datetime.get(Calendar.MINUTE) <= 9) ? "0" + datetime.get(Calendar.MINUTE) : datetime.get(Calendar.MINUTE) + "";
        return strHrsToShow + ":" + minutos + " " + am_pm;
    }

}
