package com.gabia.mbaproject.utils;

public class StringUtils {

    public static String capitalizeFirstLetter(String text) {
         return text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
