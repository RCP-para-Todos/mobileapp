package com.jk.rcp.main.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String translateDateToEasyRead(Date date) {
        DateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date actualDateTime = new java.util.Date();
        long diff = actualDateTime.getTime() - date.getTime();

        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);

        if (days <= 0) {
            if (hours <= 0) {
                if (minutes <= 0) {
                    return "Hace unos instantes";
                } else {
                    return "Hace" + minutes + " minutos";
                }
            } else {
                return "Hace " + hours + " horas";
            }
        } else if (days == 1) {
            return "Ayer";
        } else if (days > 1) {
            return "Hace " + days + " días";
        }

        return format2.format(date);
    }
}
