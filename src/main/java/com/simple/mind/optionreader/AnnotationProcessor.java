package com.simple.mind.optionreader;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import com.simple.mind.optionreader.annotations.Options;

class AnnotationProcessor {
    /**
     * Compile all available names into array, <br>
     * Possible names are: Field name<br>
     * Values set in name annotation
     * 
     * @param f
     * @return
     */
    public static String getOptionNames(Field f) {
        Options o = f.getAnnotation(Options.class);
        if (o == null || StringFormatter.isNullOrEmpty(o.name()))
            return f.getName();
        return o.name();
    }

    public static ArrayList<String> getDefaultValues(Field f) {
        ArrayList<String> toRet = new ArrayList<String>();
        Options o = f.getAnnotation(Options.class);
        if (o == null)
            return toRet;
        for (String s : o.defaultValues())
            toRet.add(s);
        return toRet;
    }

    public static String getDefaultSingleValue(Field f) {
        Options o = f.getAnnotation(Options.class);
        if (o == null)
            return null;
        if (o.defaultValues().length > 1)
            throw new RuntimeException(f.getName() + " cannot have multiple default values");
        return o.defaultValues().length == 0 ? null : o.defaultValues()[0];
    }

    public static boolean isIgnorable(Field f) {
        return isIgnorable(f.getAnnotation(Options.class));
    }

    public static boolean isIgnorable(Options o) {
        if (o == null || o.ignore() == false)
            return false;
        return true;
    }

    public static boolean isOptional(Field f) {
        Options o = f.getAnnotation(Options.class);
        if (o == null || o.optional() == true)
            return true;
        return false;
    }

    public static boolean hasDefaultValue(Field f) {
        if (f == null)
            return false;
        Options o = f.getAnnotation(Options.class);
        if (o == null)
            return false;
        if (o.defaultValues() != null && o.defaultValues().length != 0)
            return true;
        return false;
    }

    public static boolean checkFieldSanity(Field f, Options o) {
        if (o == null)
            return true;
        // Check if multiple in single platform
        String[] arr = o.defaultValues();
        if (arr.length > 1 && AcceptableList.primitives.contains(f.getType().getName())) {
            throw new RuntimeException("Expecting Single value, received multiple in default");
        }
        // check if duplicates
        ArrayList<String> arl = new ArrayList<String>();
        for (String s : arr) {
            if (arl.contains(s)) {
                throw new RuntimeException("Duplicates in default value: " + s);
            }
            arl.add(s);
        }
        return true;
    }

    public static boolean checkClassSanity(Class<?> z) {
        return checkClassSanityFields(z.getDeclaredFields());
    }

    public static boolean checkClassSanityFields(Field[] fields) {
        ArrayList<String> nameList = new ArrayList<String>();
        for (Field f : fields) {
            Options o = f.getAnnotation(Options.class);
            // check if optional and has default value
            if (isOptional(f) && o != null && hasDefaultValue(f)) {
                throw new RuntimeException(
                        "Making a variable optional and having default value is confusing. Class member: "
                                + f.getName());
            }

            // check if ignorable and has default value
            if (isIgnorable(o) && o != null && o.defaultValues() != null && o.defaultValues().length != 0) {
                throw new RuntimeException(
                        "Making a variable ignorable and having default value is confusing. Class member: "
                                + f.getName());
            }
            // check if there is duplicate name
            String name = o == null ? null : o.name();
            String varName = StringFormatter.formatName(f.getName());
            if (varName.compareTo(f.getName()) != 0) {
                throw new RuntimeException(
                        "This tool rejects snake style variable declaration. This is not ideal; but this seems safer. Rejected variable name: "
                                + f.getName());
            }
            if (nameList.contains(varName)) {
                throw new RuntimeException("Duplicate name: " + varName
                        + "; make sure member variable name does not conflict with other command option name");
            } else {
                nameList.add(varName);
            }

            if (nameList.contains(name)) {
                throw new RuntimeException("Duplicate name: " + name
                        + "; make sure member variable name does not conflict with other command option name");
            } else {
                nameList.add(name);
            }

            checkFieldSanity(f, o);
        }
        return false;
    }

    public static String isValidListGenericType(Field f) {
        ParameterizedType pramType = (ParameterizedType) f.getGenericType();
        Class<?> listGenType = (Class<?>) pramType.getActualTypeArguments()[0];
        if (AcceptableList.primitives.contains(listGenType.getName())) {
            return listGenType.getName();
        }

        throw new RuntimeException("Invalid list Type: " + listGenType.getName());
    }
}
