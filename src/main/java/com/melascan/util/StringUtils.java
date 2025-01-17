package com.melascan.util;

public class StringUtils {

    private StringUtils() {
    }

    public static Boolean isEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    public static Boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
