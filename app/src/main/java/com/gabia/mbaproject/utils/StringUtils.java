package com.gabia.mbaproject.utils;

import java.text.Normalizer;

public class StringUtils {

    public static String capitalizeFirstLetter(String text) {
         return text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static String unAccent(String src) {
        return Normalizer
                .normalize(src, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }
}
