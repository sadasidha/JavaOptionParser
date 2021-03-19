package com.simple.mind.optionreader;

class StringFormatter {
    public static String formatName(String s) {
        String[] arr = s.replaceAll("^--", "").split("_");
        StringBuilder sb = null;
        for (String p : arr) {
            if (sb == null)
                sb = new StringBuilder(p);
            else
                sb.append(p.substring(0, 1).toUpperCase()).append(p.substring(1));
        }
        return sb == null ? "" : sb.toString();
    }

    static boolean isNullOrEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }
}
