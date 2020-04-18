package com.simple.mind.optionreader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringProcessor {
	public static class NamgeLength {
		public String name;
		public String length;
	}

	/**
	 * Extract the parameter name and suggested length<br>
	 * 
	 * @param s : --file-3
	 * @return: ["file", "3"]
	 * @throws Exception
	 */
	public static NamgeLength getLengthAndName(String s) throws Exception {
		// look for major
		NamgeLength toRet = new NamgeLength();
		Matcher matcher = Pattern.compile("^(--.+)?-([0-9]+)$").matcher(s);
		if (matcher.matches()) {
			toRet.name = matcher.group(1);
			toRet.length = matcher.group(2);
			return toRet;
		}
		// look for minor
		matcher = Pattern.compile("^(-.)-?([0-9]+)$").matcher(s);
		if (matcher.matches()) {
			toRet.name = matcher.group(1);
			toRet.length = matcher.group(2);
			return toRet;
		}
		throw new Exception("Invalid Pattarn");
	}

	public static String stripHyphen(String s) {
		Pattern p = Pattern.compile("^([-]{1,2})");
		Matcher m = p.matcher(s);
		if (m.find()) {
			s = m.replaceFirst(""); // number 46
		}
		return getProperFieldName1(s);
	}

	public static String formatName(String s) {
		return getProperFieldName1(stripHyphen(s));
	}

	/**
	 * @param name : a_b
	 * @return: aB
	 */
	public static String getProperFieldName1(String name) {
		StringBuilder sb = new StringBuilder();
		boolean prev = false;
		for (int i = 0; i < name.length(); i++) {
			if (prev == true) {
				sb.append(String.valueOf(name.charAt(i)).toUpperCase());
				prev = false;
			} else if (name.charAt(i) == '_') {
				prev = true;
			} else {
				sb.append(String.valueOf(name.charAt(i)));
				prev = false;
			}
		}
		return sb.toString();
	}
}
