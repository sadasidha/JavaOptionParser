package com.simple.mind.optionreader;

import java.math.BigInteger;

import com.google.common.base.Strings;

class TypeConverter {
    private static final String MATCH_ANY_VALID_NUMBER = "^[+-]{0,1}\\d*\\.?\\d*$";
    private static final String MATCH_NUMBER = "^[+-]{0,1}\\d+$";
    private static final int MAX_COMP = 1;
    private static final int MIN_COMP = -1;
    private static final String MSG = "Invalid Number is passed. Found: ";

    static boolean isNumber(String value) {
        if (!value.matches(MATCH_NUMBER)) {
            throw new RuntimeException(MSG + value);
        }
        return true;
    }

    static boolean isNumeric(String value) {
        if (!value.matches(MATCH_ANY_VALID_NUMBER)) {
            throw new RuntimeException(MSG + value);
        }
        return true;
    }

    static boolean inRange(String value, long max, long min) {
        BigInteger b = new BigInteger(value);
        if (b.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) == MAX_COMP) {
            throw new RuntimeException("Number is too big for integer. Found: " + value);
        }
        if (b.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) == MIN_COMP) {
            throw new RuntimeException("Number is too small for integer. Found: " + value);
        }
        return true;
    }

    public static BigInteger toBigInt(String value) {
        isNumber(value);
        return new BigInteger(value);
    }

    public static Integer toInt(String value) {
        isNumber(value);
        inRange(value, Integer.MAX_VALUE, Integer.MIN_VALUE);
        return Integer.valueOf(value);
    }

    public static Long toLong(String value) {
        isNumber(value);
        inRange(value, Long.MAX_VALUE, Long.MIN_VALUE);
        return Long.valueOf(value);
    }

    public static Byte toByte(String value) {
        isNumber(value);
        inRange(value, Byte.MAX_VALUE, Byte.MIN_VALUE);
        return Byte.valueOf(value);
    }

    public static Float toFloat(String value) {
        isNumeric(value);
        return Float.valueOf(value);
    }

    public static Double toDouble(String value) {
        isNumeric(value);
        return Double.valueOf(value);
    }

    public static Character toChar(String value) {
        if (value.length() != 1)
            throw new RuntimeException("Too long for character Type. Found: " + value);
        return value.charAt(0);
    }

    public static Boolean toBoolean(String value) {
        switch (Strings.nullToEmpty(value).trim().toLowerCase()) {
        case "":
            return true;
        case "no":
        case "false":
            return false;
        case "true":
        case "yes":
            return true;
        }
        throw new RuntimeException("Invalid Boolean value. Found: " + value);
    }
}
