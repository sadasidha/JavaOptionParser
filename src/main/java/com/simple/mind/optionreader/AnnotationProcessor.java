package com.simple.mind.optionreader;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.simple.mind.optionreader.annotations.Options;

public class AnnotationProcessor {
	/**
	 * Compile all available names into array, <br>
	 * Possible names are: Field name<br>
	 * Values set in name annotation
	 * 
	 * @param f
	 * @return
	 */
	public static ArrayList<String> getOptionNames(Field f) {
		Options o = f.getAnnotation(Options.class);
		ArrayList<String> t = new ArrayList<String>();
		t.add(f.getName());
		if (o == null) {
			return t;
		}
		for (String n : o.name()) {
			if (!t.contains(n))
				t.add(n);
		}
		return t;
	}

	public static ArrayList<String> getDefaultValues(Field f) {
		ArrayList<String> toRet = new ArrayList<String>();
		Options o = f.getAnnotation(Options.class);
		if (o == null) {
			return toRet;
		}
		for (String s : o.defaultValues()) {
			toRet.add(s);
		}
		return toRet;
	}

	public static boolean isIgnorable(Field f) {
		Options o = f.getAnnotation(Options.class);
		return isIgnorable(o);
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

	public static boolean isHasDefaultValue(Field f) {
		Options o = f.getAnnotation(Options.class);
		if (o == null)
			return false;
		if (o.defaultValues() != null && o.defaultValues().length != 0)
			return true;
		return true;
	}

	public static boolean checkFieldSanity(Field f, Options o) throws Exception {
		if (o == null)
			return true;
		// Check if multiple in single platform
		String[] arr = o.defaultValues();
		if (arr.length > 1 && AcceptableList.primitives.contains(f.getType().getName())) {
			throw new Exception("Expecting Single value, received multiple in default");
		}
		// check if duplicates
		ArrayList<String> arl = new ArrayList<String>();
		for (String s : arr) {
			if (arl.contains(s)) {
				throw new Exception("Duplicates in default value: " + s);
			}
			arl.add(s);
		}
		return true;
	}

	public static boolean checkClassSanity(Class<?> z) throws Exception {
		return checkClassSanity(z);
	}

	public static boolean checkClassSanity(Field[] fields) throws Exception {
		ArrayList<String> nameList = new ArrayList<String>();
		for (Field f : fields) {
			Options o = f.getAnnotation(Options.class);
			// check if optional and has default value
			if (isOptional(f) && o != null && isHasDefaultValue(f)) {
				throw new Exception("Making a variable optional and having default value is confusing. Class member: "
						+ f.getName());
			}
			// check if ignorable and has default value
			if (isIgnorable(o) && o != null && o.defaultValues() != null) {
				throw new Exception("Making a variable ignorable and having default value is confusing. Class member: "
						+ f.getName());
			}
			// check if there is duplicate name
			String[] names = o == null ? null : o.name();
			if (names != null) {
				for (String name : names) {
					if (nameList.contains(name)) {
						throw new Exception("Duplicate Name: " + name);
					}
				}
			}

			checkFieldSanity(f, o);

		}
		return false;
	}
}
