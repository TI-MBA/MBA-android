package com.gabia.mbaproject.application.modules.admin.utils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ArrayFilterUtils {

    public static <T> List<T> arrayFilter(List<T> list, Predicate<T> predicate) {
        return list
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
