package com.gabia.mbaproject.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    public static String datePattern = "dd/MM/yyyy HH:mm:ss";
    public static String isoDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static String monthAndYear = "MM/yyyy";
    public static String brazilianDate = "dd/MM/yyyy";

    public static Date toDate(String pattern, String date) {
        if (date == null) { return new Date(); }
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String toString(String pattern, Date date) {
        if (date == null) { return "Data sem valor"; }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static String changeFromIso(String pattern, String date) {
        if (date == null) { return "Data sem valor"; }
        Date dateResult = toDate(isoDateFormat, date);
        return toString(pattern, dateResult);
    }
}