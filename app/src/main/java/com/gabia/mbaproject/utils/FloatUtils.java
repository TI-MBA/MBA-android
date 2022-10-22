package com.gabia.mbaproject.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FloatUtils {

    public static String moneyFormat(Float value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyValue = formatter.format(value);
        return "R" + moneyValue;
    }
}
