package com.simple.mind.optionreader;

import java.util.ArrayList;

public class AcceptableList {
	public static final ArrayList<String> listTypes;
	public static final ArrayList<String> primitives;
	static {
		listTypes = new ArrayList<String>();
		listTypes.add("java.util.List");
		listTypes.add("java.util.ArrayList");
		primitives = new ArrayList<String>();
		primitives.add("int");
		primitives.add("java.lang.Integer");
		primitives.add("long");
		primitives.add("java.lang.Long");
		primitives.add("byte");
		primitives.add("java.lang.Byte");
		primitives.add("char");
		primitives.add("java.lang.Character");
		primitives.add("float");
		primitives.add("java.lang.Float");
		primitives.add("double");
		primitives.add("java.lang.Double");
		primitives.add("boolean");
		primitives.add("java.lang.Boolean");
		primitives.add("java.lang.String");
		primitives.add("java.math.BigInteger");
	}
}
