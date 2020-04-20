package com.simple.mind.optionreader;

import java.math.BigInteger;

public class ParseValueToPrimary {
	private static final String MATCH_ANY_VALID_NUMBER = "^[+-]{0,1}\\d*\\.?\\d*$";
	private static final String MATCH_NUMBER = "^[+-]{0,1}\\d+$";
	private static final int MAX_COMP = 1;
	private static final int MIN_COMP = -1;
	private static final String MSG = "Invalid Number is passed. Found: ";

	public static BigInteger convertToBigInt(String value) throws Exception {
		if (!value.matches(MATCH_NUMBER)) {
			throw new Exception(MSG + value);
		}
		return new BigInteger(value);
	}

	public static Integer convertToInt(String value) throws Exception {
		if (!value.matches(MATCH_NUMBER)) {
			throw new Exception(MSG + value);
		}
		if (new BigInteger(value).compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) == MAX_COMP) {
			throw new Exception("Number is too big for integer. Found: " + value);
		}
		if (new BigInteger(value).compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) == MIN_COMP) {
			throw new Exception("Number is too small for integer. Found: " + value);
		}
		return Integer.valueOf(value);
	}

	public static Long convertToLong(String value) throws Exception {
		if (!value.matches(MATCH_NUMBER)) {
			throw new Exception(MSG + value);
		}
		if (new BigInteger(value).compareTo(BigInteger.valueOf(Long.MAX_VALUE)) == MAX_COMP) {
			throw new Exception("Number is too big for Long. Found: " + value);
		}
		if (new BigInteger(value).compareTo(BigInteger.valueOf(Long.MIN_VALUE)) == MIN_COMP) {
			throw new Exception("Number is too small for Long. Found: " + value);
		}
		return Long.valueOf(value);
	}

	public static Byte convertToByte(String value) throws Exception {
		if (!value.matches(MATCH_NUMBER)) {
			throw new Exception(MSG + value);
		}
		if (new BigInteger(value).compareTo(BigInteger.valueOf(Byte.MAX_VALUE)) == MAX_COMP) {
			throw new Exception("Number is too big for Byte. Found: " + value);
		}
		if (new BigInteger(value).compareTo(BigInteger.valueOf(Byte.MIN_VALUE)) == MIN_COMP) {
			throw new Exception("Number is too small for Byte. Found: " + value);
		}
		return Byte.valueOf(value);
	}

	public static Float convertToFloat(String value) throws Exception {
		if (!value.matches(MATCH_ANY_VALID_NUMBER)) {
			throw new Exception("Not valid float value. Found: " + value);
		}
		return Float.valueOf(value);
	}

	public static Double convertToDouble(String value) throws Exception {
		if (!value.matches(MATCH_ANY_VALID_NUMBER)) {
			throw new Exception("Not valid Double value. Found: " + value);
		}
		return Double.valueOf(value);
	}

	public static Character convertToChar(String value) throws Exception {
		if (value.length() != 1) {
			throw new Exception("Too long for character Type. Found: " + value);
		}
		return value.charAt(0);
	}

	public static Boolean convertToBoolean(String value) throws Exception {
		if (value.isBlank() || value.trim().toLowerCase().compareTo("false") == 0 || value.trim().compareTo("0") == 0)
			return false;
		if (value.trim().toLowerCase().compareTo("true") == 0 || value.trim().toLowerCase().compareTo("1") == 0)
			return true;
		throw new Exception("Invalid Boolean value. Found: " + value);
	}

}
