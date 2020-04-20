package com.simple.mind.optionreader;

import java.math.BigInteger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ParseNumericValueTest extends TestCase {

	public ParseNumericValueTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(ParseNumericValueTest.class);
	}

	public void testConvertToBigInt() {
		try {
			ParseValueToPrimary.convertToBigInt("abc");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid Number is passed. Found: abc");
		}

		try {
			assertEquals(ParseValueToPrimary.convertToBigInt("99559955995599559955")
					.compareTo(new BigInteger("99559955995599559955")), 0);
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public void testConvertToInt() {
		try {
			ParseValueToPrimary.convertToInt("abc");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid Number is passed. Found: abc");
		}

		try {
			ParseValueToPrimary.convertToInt("99559955995599559955");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Number is too big for integer. Found: 99559955995599559955");
		}

		try {
			ParseValueToPrimary.convertToInt("-99559955995599559955");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Number is too small for integer. Found: -99559955995599559955");
		}

		try {
			assertEquals(ParseValueToPrimary.convertToInt("5000"), Integer.valueOf(5000));
			assertEquals(ParseValueToPrimary.convertToInt("-5000"), Integer.valueOf(-5000));
			assertEquals(ParseValueToPrimary.convertToInt("+5000"), Integer.valueOf(5000));
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public void testConvertToLong() {
		try {
			ParseValueToPrimary.convertToLong("abc");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid Number is passed. Found: abc");
		}

		try {
			ParseValueToPrimary.convertToLong("99559955995599559955");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Number is too big for Long. Found: 99559955995599559955");
		}

		try {
			ParseValueToPrimary.convertToLong("-99559955995599559955");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Number is too small for Long. Found: -99559955995599559955");
		}

		try {
			assertEquals(ParseValueToPrimary.convertToLong("5000"), Long.valueOf(5000));
			assertEquals(ParseValueToPrimary.convertToLong("-5000"), Long.valueOf(-5000));
			assertEquals(ParseValueToPrimary.convertToLong("+5000"), Long.valueOf(5000));
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public void testConvertToByte() {
		try {
			ParseValueToPrimary.convertToByte("abc");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid Number is passed. Found: abc");
		}

		try {
			ParseValueToPrimary.convertToByte("257");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Number is too big for Byte. Found: 257");
		}

		try {
			ParseValueToPrimary.convertToByte("-257");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Number is too small for Byte. Found: -257");
		}

		try {
			assertEquals(ParseValueToPrimary.convertToByte("127"), Byte.valueOf((byte) 127));
			assertEquals(ParseValueToPrimary.convertToByte("-127"), Byte.valueOf((byte) -127));
			assertEquals(ParseValueToPrimary.convertToByte("+127"), Byte.valueOf((byte) 127));
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public void testConvertToFloat() {
		try {
			ParseValueToPrimary.convertToFloat("abc");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Not valid float value. Found: abc");
		}

		try {
			assertEquals(ParseValueToPrimary.convertToFloat("127.0"), Float.valueOf(127.0f));
			assertEquals(ParseValueToPrimary.convertToFloat("-127.3"), Float.valueOf(-127.3f));
			assertEquals(ParseValueToPrimary.convertToFloat("+127.5"), Float.valueOf(127.5f));
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public static void testConvertToDouble() {
		try {
			ParseValueToPrimary.convertToDouble("abc");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Not valid Double value. Found: abc");
		}

		try {
			assertEquals(ParseValueToPrimary.convertToDouble("127.0"), Double.valueOf(127.0));
			assertEquals(ParseValueToPrimary.convertToDouble("-127.3"), Double.valueOf(-127.3));
			assertEquals(ParseValueToPrimary.convertToDouble("+127.5"), Double.valueOf(127.5));
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public static void testConvertToChar() {
		try {
			ParseValueToPrimary.convertToChar("abc");
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Too long for character Type. Found: abc");
		}

		try {
			assertEquals(ParseValueToPrimary.convertToChar("1"), Character.valueOf('1'));
			assertEquals(ParseValueToPrimary.convertToChar("-"), Character.valueOf('-'));
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public static void testConvertToBoolean() {
	}

}
