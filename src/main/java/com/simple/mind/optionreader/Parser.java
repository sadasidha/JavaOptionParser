package com.simple.mind.optionreader;

import static com.simple.mind.optionreader.StringProcessor.formatName;
import static com.simple.mind.optionreader.StringProcessor.getLengthAndName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.simple.mind.optionreader.StringProcessor.NamgeLength;

public class Parser {
	public static HashMap<String, ArrayList<String>> processArgs(String[] args) throws Exception {
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		for (int i = 0; i < args.length; i++) {
			if (!args[i].matches("^[a-zA-Z0-9-_]+$")) {
				throw new Exception(
						"Parameter name should not contain any non alpha numeric or non hyphen or non underscore characetr. Found: " + args[i]);
			}
			/*
			 * Array --array-10 a b c d e f g h i j
			 */
			if (args[i].matches("^--.+?-[0-9]+$") || args[i].matches("^-[^-][a-zA-Z]-[0-9]+$")) {
				NamgeLength t = getLengthAndName(args[i]);
				String optionName = formatName(t.name);
				int length = Integer.valueOf(t.length);
				ArrayList<String> l = map.get(optionName);
				if (length + i >= args.length) {
					throw new Exception(
							"Number of value does not match for " + optionName + "; expected " + length + " items");
				}
				if (l == null) {
					l = new ArrayList<String>();
					map.put(optionName, l);
				}
				i++;
				for (; length > 0; length--, i++) {
					l.add(args[i]);
				}
				i--;
			}
			/*
			 * Array --array[ a b c d e "]"
			 */
			else if (args[i].matches("^--.*?\\[$") || args[i].matches("^-[^-]\\[$")) {
				Pattern pattern = Pattern.compile("^(.+)?\\[$");
				Matcher matcher = pattern.matcher(args[i]);
				String optionName;
				if (matcher.matches()) {
					optionName = formatName(matcher.group(1).toLowerCase());
				} else {
					throw new Exception("Bad input");
				}
				ArrayList<String> l = map.get(optionName);
				if (l == null) {
					l = new ArrayList<String>();
					map.put(optionName, l);
				}
				boolean end = false;
				for (i++; i < args.length; i++) {
					if (args[i].compareTo("]") == 0) {
						end = true;
						break;
					}
					// adding option values
					l.add(args[i]);
				}
				if (!end) {
					throw new Exception("Invalid input");
				}
			} else if (args[i].matches("^--.+$")) {
				/*
				 * --debug
				 */
				if (args.length <= i + 1 || args[i + 1].matches("^--.+$")) {
					String optionName = formatName(args[i]).toLowerCase();
					// boolean found
					// optionName
					ArrayList<String> l = map.get(optionName);
					if (l == null) {
						l = new ArrayList<String>();
						map.put(optionName, l);
					}
					// adding next value
					l.add("true");
				} else {
					// next value belongs
					String optionName = formatName(args[i]).toLowerCase();
					ArrayList<String> l = map.get(optionName);
					if (l == null) {
						l = new ArrayList<String>();
						map.put(optionName, l);
					}
					// adding next value
					i++;
					l.add(args[i]);
				}
			} else {
				throw new Exception("Invalid option " + args[i]);
			}
		}
		return map;
	}
}
