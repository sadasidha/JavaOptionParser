package com.simple.mind.optionreader;

import static com.simple.mind.optionreader.ParseNumericValue.convertToInt;
import static com.simple.mind.optionreader.ParseNumericValue.convertToLong;
import static com.simple.mind.optionreader.ParseNumericValue.convertToFloat;
import static com.simple.mind.optionreader.ParseNumericValue.convertToDouble;
import static com.simple.mind.optionreader.ParseNumericValue.convertToByte;
import static com.simple.mind.optionreader.ParseNumericValue.convertToChar;
import static com.simple.mind.optionreader.ParseNumericValue.convertToBoolean;

import static com.simple.mind.optionreader.AnnotationProcessor.getOptionNames;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OptionsParser<T> {
	private static HashMap<String, ArrayList<String>> map;

	/**
	 * Parse arguments, do not throw exception; print the exception on console.
	 * 
	 * @param <T>
	 * @param args
	 * @param c
	 * @return
	 */
	public static <T> T ReadOptionNE(String[] args, Class<T> c) {
		T obj = null;
		try {
			obj = ParseOption(args, c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * @param <T>  Generic Class
	 * @param args Array of string to be parsed
	 * @param c    : Class Type
	 * @return: Returns Object of class
	 * @throws Exception: Throws Exception
	 */
	public static <T> T ParseOption(String[] args, Class<T> c) throws Exception {
		map = CommandParser.processArgs(args);
		return prepareObject(c);
	}

	private static boolean isArrayOrList(Field f) {
		if (f.getType().isArray()) {
			return true;
		}

		if (AcceptableList.listTypes.contains(f.getType().getName())) {
			return true;
		}
		return false;
	}

	/**
	 * @param <T> Class that needed to be assigned
	 * @param c   Class
	 * @return Class Object
	 * @throws Exception
	 */
	private static <T> T prepareObject(Class<T> c) throws Exception {
		T obj = null;
		obj = c.getDeclaredConstructor().newInstance();
		Field[] df = c.getDeclaredFields();
		AnnotationProcessor.checkClassSanity(df);
		for (Field f : df) {
			if (AnnotationProcessor.isIgnorable(f)) {
				continue;
			}
			boolean isArrList = isArrayOrList(f);
			if (!isArrList)
				handleNonArray(obj, f);
			else {
				setListAndArray(obj, f);
			}
		}
		return obj;
	}

	/**
	 * Compile all value by names into single array and return;
	 * 
	 * @param name array: can be compiled by getOptionNames
	 * @return
	 */
	// TODO fUCK THIS
	private static ArrayList<String> getValues(ArrayList<String> names) {
		ArrayList<String> toRet = new ArrayList<String>();
		for (String name : names) {
			if (map.get(name) != null) {
				for (String s : map.get(name)) {
					toRet.add(s);
				}
			}
		}
		return toRet;
	}

	private static <T> void handleNonArray(T obj, Field f) throws Exception {
		ArrayList<String> values = getValues(getOptionNames(f));

		if (values == null || values.size() == 0) {
			values = AnnotationProcessor.getDefaultValues(f);
		}

		if (values.size() != 1) {
			if (values.size() != 1) {
				throw new Exception(
						f.getName() + " should be single value, but set multiple values or does not exists");
			}
		}

		String value = values.get(0);
		boolean isPr = f.getType().isPrimitive();
		if (f.getType().equals(Integer.TYPE) || f.getType().isAssignableFrom(Integer.class)) {
			boolean b = f.canAccess(obj);
			f.setAccessible(true);
			if (isPr)
				f.setInt(obj, (int) convertToInt(value));
			else
				f.set(obj, convertToInt(value));
			f.setAccessible(b);
			return;
		}
		if (f.getType().equals(Long.TYPE) || f.getType().isAssignableFrom(Long.class)) {
			boolean b = f.canAccess(obj);
			f.setAccessible(true);
			if (isPr)
				f.setLong(obj, (long) convertToLong(value));
			else
				f.setLong(obj, convertToLong(value));
			f.setAccessible(b);
			return;
		}
		if (f.getType().isAssignableFrom(String.class)) {
			boolean b = f.canAccess(obj);
			f.setAccessible(true);
			f.set(obj, value);
			f.setAccessible(b);
			return;
		}

		if (f.getType().isAssignableFrom(BigInteger.class)) {
			boolean b = f.canAccess(obj);
			f.setAccessible(true);
			f.set(obj, ParseNumericValue.convertToBigInt(value));
			f.setAccessible(b);
			return;
		}
		if (f.getType().equals(Character.TYPE) || f.getType().isAssignableFrom(Character.class)) {
			boolean b = f.canAccess(obj);
			f.setAccessible(true);
			if (isPr)
				f.setChar(obj, (char) convertToChar(value));
			else
				f.setChar(obj, convertToChar(value));
			f.setAccessible(b);
			return;
		}
		if (f.getType().equals(Byte.TYPE) || f.getType().isAssignableFrom(Byte.class)) {
			boolean b = f.canAccess(obj);
			f.setAccessible(true);
			if (isPr)
				f.setByte(obj, (byte) convertToByte(value));
			else
				f.setByte(obj, convertToByte(value));
			f.setAccessible(b);
			return;
		}
		if (f.getType().equals(Double.TYPE) || f.getType().isAssignableFrom(Double.class)) {
			boolean b = f.canAccess(obj);
			f.setAccessible(true);
			if (isPr)
				f.setDouble(obj, (double) convertToDouble(value));
			else
				f.setDouble(obj, convertToDouble(value));
			f.setAccessible(b);
			return;
		}
		if (f.getType().equals(Float.TYPE) || f.getType().isAssignableFrom(Float.class)) {
			boolean b = f.canAccess(obj);
			f.setAccessible(true);
			if (isPr)
				f.setDouble(obj, (float) convertToFloat(value));
			else
				f.setDouble(obj, convertToFloat(value));
			f.setAccessible(b);
			return;
		}
		if (f.getType().equals(Boolean.TYPE) || f.getType().isAssignableFrom(Boolean.class)) {
			boolean b = f.canAccess(obj);
			f.setAccessible(true);
			if (isPr)
				f.setBoolean(obj, (boolean) convertToBoolean(value));
			else
				f.setBoolean(obj, convertToBoolean(value));
			f.setAccessible(b);
			return;
		}
	}

	// TODO
	private static String isValidListGenericType(Field f) throws Exception {
		ParameterizedType pramType = (ParameterizedType) f.getGenericType();
		Class<?> listGenType = (Class<?>) pramType.getActualTypeArguments()[0];
		if (AcceptableList.primitives.contains(listGenType.getName())) {
			return listGenType.getName();
		}

		throw new Exception("Invalid list Type: " + listGenType.getName());
	}

	// TODO
	private static <T> void setListAndArray(T obj, Field f) throws Exception {
		String primitiveType;
		if (f.getType().isArray()) {
			primitiveType = f.getType().getComponentType().getName();
		} else {
			primitiveType = f.getType().getName();
		}

		ArrayList<String> valArr = getValues(getOptionNames(f));

		// I do not expect valArr to be null
		if (valArr.size() == 0 && AnnotationProcessor.isOptional(f) == false
				&& !AnnotationProcessor.hasDefaultValue(f)) {
			throw new Exception(f.getName()
					+ " is not optional, does not have default values and no value is passed through command line");
		} else if (valArr == null || valArr.size() == 0) {
			valArr = AnnotationProcessor.getDefaultValues(f);
			if (valArr.size() == 0) {
				throw new Exception(
						f.getName() + " default does not exists and no value is passed through command line");
			}
		}

		if (AcceptableList.primitives.contains(primitiveType)) {
			if (primitiveType.compareTo("int") == 0) {
				int[] intArr = new int[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToInt(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("java.lang.Integer") == 0) {
				Integer[] intArr = new Integer[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToInt(valArr.get(i));
				}
				f.set(obj, intArr);
				return;
			}
			if (primitiveType.compareTo("long") == 0) {
				long[] intArr = new long[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToLong(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("java.lang.Long") == 0) {
				long[] intArr = new long[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToLong(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("byte") == 0) {
				byte[] intArr = new byte[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToByte(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("java.lang.Byte") == 0) {
				Byte[] intArr = new Byte[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToByte(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("char") == 0) {
				char[] intArr = new char[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToChar(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("java.lang.Character") == 0) {
				Character[] intArr = new Character[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToChar(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("float") == 0) {
				float[] intArr = new float[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToFloat(valArr.get(i));
				}
				f.set(obj, intArr);
				return;
			}
			if (primitiveType.compareTo("java.lang.Float") == 0) {
				Float[] intArr = new Float[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToFloat(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("double") == 0) {
				double[] intArr = new double[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToDouble(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("java.lang.Double") == 0) {
				Double[] intArr = new Double[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToDouble(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("boolean") == 0) {
				boolean[] intArr = new boolean[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToBoolean(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("java.lang.Boolean") == 0) {
				Boolean[] intArr = new Boolean[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = convertToBoolean(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("java.lang.String") == 0) {
				String[] intArr = new String[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = valArr.get(i);
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("java.math.BigInteger") == 0) {
				BigInteger[] intArr = new BigInteger[valArr.size()];
				for (int i = 0; i < valArr.size(); i++) {
					intArr[i] = ParseNumericValue.convertToBigInt(valArr.get(i));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
		} else if (AcceptableList.listTypes.contains(primitiveType)) {
			String type = isValidListGenericType(f);

			if (type.compareTo("java.lang.Integer") == 0) {
				List<Integer> intArr = new ArrayList<Integer>();
				for (int i = 0; i < valArr.size(); i++) {
					intArr.add(convertToInt(valArr.get(i)));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (type.compareTo("java.lang.Long") == 0) {
				List<Long> intArr = new ArrayList<Long>();
				for (int i = 0; i < valArr.size(); i++) {
					intArr.add(convertToLong(valArr.get(i)));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}

			if (type.compareTo("java.lang.Byte") == 0) {
				List<Byte> intArr = new ArrayList<Byte>();
				;
				for (int i = 0; i < valArr.size(); i++) {
					intArr.add(convertToByte(valArr.get(i)));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (type.compareTo("java.lang.Character") == 0) {
				List<Character> intArr = new ArrayList<Character>();
				for (int i = 0; i < valArr.size(); i++) {
					intArr.add(convertToChar(valArr.get(i)));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (type.compareTo("java.lang.Float") == 0) {
				List<Float> intArr = new ArrayList<Float>();
				for (int i = 0; i < valArr.size(); i++) {
					intArr.add(convertToFloat(valArr.get(i)));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (type.compareTo("java.lang.Double") == 0) {
				List<Double> intArr = new ArrayList<Double>();
				for (int i = 0; i < valArr.size(); i++) {
					intArr.add(convertToDouble(valArr.get(i)));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (type.compareTo("java.lang.String") == 0) {
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, valArr);
				f.setAccessible(b);
				return;
			}

			if (type.compareTo("java.lang.Boolean") == 0) {
				List<Boolean> intArr = new ArrayList<Boolean>();
				for (int i = 0; i < valArr.size(); i++) {
					intArr.add(Boolean.valueOf(valArr.get(i)));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
			if (primitiveType.compareTo("java.math.BigInteger") == 0) {
				List<BigInteger> intArr = new ArrayList<BigInteger>();
				for (int i = 0; i < valArr.size(); i++) {
					intArr.add(ParseNumericValue.convertToBigInt(valArr.get(i)));
				}
				boolean b = f.canAccess(obj);
				f.setAccessible(true);
				f.set(obj, intArr);
				f.setAccessible(b);
				return;
			}
		}
		throw new Exception("Type defined in class is not valid: " + primitiveType);
	}

}
