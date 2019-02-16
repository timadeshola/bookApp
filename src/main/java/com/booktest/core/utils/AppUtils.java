package com.booktest.core.utils;

import com.google.gson.Gson;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class AppUtils {

    public static final SimpleDateFormat FORMATTER_ = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String capitalise(String data) {
        if (StringUtils.isEmpty(data)) {
            return data;
        }
        return data.toUpperCase();
    }

    public static String generateUUIDNumber() {
        String randomString = UUID.randomUUID().toString();
        String[] splitArrays =randomString.split("-");
        String secondIndex = splitArrays[2];
        String thirdIndex = splitArrays[3];
        String values = secondIndex + "-" + thirdIndex;
        return AppUtils.capitalise(values);
    }

    public static <T> String toJSON(T t) {
        return new Gson().toJson(t);
    }

}
