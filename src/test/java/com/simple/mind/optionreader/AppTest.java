package com.simple.mind.optionreader;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.simple.mind.optionreader.annotations.Options;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public static class Arguments {
		@Options(name = { "books" }, optional = false)
		public ArrayList<String> b;
		public List<String> fileNames;
		public boolean debug;
		@Options(name = "debug2")
		public boolean d2bug;
		public Integer in;
		@Options(ignore = true)
		public static boolean ignore;
	}

	public void testValidArgs() {
		try {
			String[] args = new String[] { "--file_names[", "file-one", "file-two", "]", "--debug", "--in", "10",
					"--fileNames-3", "file-three", "file-four", "file-v", "--debug2", "--books", "blue" };
			Arguments ar = OptionsParser.ParseOption(args, Arguments.class);
			assertEquals(ar.d2bug, true);
			assertEquals(ar.debug, true);
			assertEquals(ar.fileNames.size(), 5);
			assertEquals(ar.b.size(), 1);
			assertEquals(ar.b.get(0), "blue");
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public static class ClsToTest1 {
		@Options(defaultValues = "true")
		boolean debug;
	}

	public static class NodefToFail {
		boolean debug;
	}

	public static class ClsToTest2 {
		@Options(defaultValues = { "true", "false" })
		boolean debug;
	}

	public static class ClsToTest4 {
		@Options(defaultValues = { "true", "false" })
		boolean[] mult1;
		@Options(defaultValues = { "true", "false" })
		Boolean[] mult2;
		@Options(defaultValues = { "FALSE", "true" })
		Boolean[] mult3;
		Boolean[] mult4;
	}

	public void testObject() {
		try {
			OptionsParser.ParseOption(new String[] {}, NodefToFail.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "debug should be single value, but set multiple values or does not exists");
		}
		try {
			OptionsParser.ParseOption(new String[] { "--abc def" }, ClsToTest1.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(),
					"Parameter name should not contain any non alpha numeric or non hyphen or non underscore characetr. Found: --abc def");
		}
		try {
			ClsToTest1 clsToTest1_1 = null;
			clsToTest1_1 = OptionsParser.ParseOption(new String[] {}, ClsToTest1.class);
			assertTrue(clsToTest1_1.debug);
			clsToTest1_1 = OptionsParser.ParseOption(new String[] { "--debug", "false" }, ClsToTest1.class);
			assertFalse(clsToTest1_1.debug);
			clsToTest1_1 = OptionsParser.ParseOption(new String[] { "--debug", "true" }, ClsToTest1.class);
			assertTrue(clsToTest1_1.debug);
		} catch (Exception e) {
			fail("Should not be here");
		}
		try {
			OptionsParser.ParseOption(new String[] { "--debug", "FLUE" }, ClsToTest1.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid Boolean value. Found: FLUE");
		}

		try {
			OptionsParser.ParseOption(new String[] {}, ClsToTest2.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Expecting Single value, received multiple in default");
		}

		ClsToTest4 cls4 = null;
		try {
			cls4 = OptionsParser.ParseOption(new String[] { "--mult1", "--mult1", "--mult2", "--mult2", "true",
					"--mult2", "false", "--mult3", "--mult4", "false" }, ClsToTest4.class);
			assertEquals(cls4.mult1.length, 2);
			assertTrue(cls4.mult1[0]);
			assertTrue(cls4.mult1[1]);
			assertTrue(cls4.mult2.length == 3);
			assertTrue(cls4.mult2[0]);
			assertTrue(cls4.mult2[1]);
			assertFalse(cls4.mult2[2]);
			assertTrue(cls4.mult3.length == 1);
			assertTrue(cls4.mult3[0]);
			assertTrue(cls4.mult4.length == 1);
			assertFalse(cls4.mult4[0]);
		} catch (Exception e) {
			fail("Should not be here");
		}

		try {
			cls4 = OptionsParser.ParseOption(
					new String[] { "--mult1", "--mult1", "--mult2", "--mult2", "true", "--mult2", "false", "--mult3" },
					ClsToTest4.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "mult4 default does not exists and no value is passed through command line");
		}
	}

	public static class CheckBigInt {
		@Options(defaultValues = { "10", "9000000000000000000000000" })
		BigInteger[] value1;
		BigInteger value2;
	}

	public static class CheckIntegerFail {
		@Options(defaultValues = { "10", "9000000000000000000000000" })
		int[] value1;
	}

	public void testCheckMathValues() {
		try {
			OptionsParser.ParseOption(new String[] { "--value2" }, CheckBigInt.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid Number is passed. Found: true");
		}

		try {
			OptionsParser.ParseOption(new String[] { "--value2-2", "20", "90" }, CheckBigInt.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "value2 should be single value, but set multiple values or does not exists");
		}

		try {
			CheckBigInt cb1 = OptionsParser.ParseOption(
					new String[] { "--value1-2", "20", "90", "--value2", "12345678910111213141516178" },
					CheckBigInt.class);
			assertEquals(cb1.value1[0], BigInteger.valueOf(20));
			assertEquals(cb1.value1[1], BigInteger.valueOf(90));
			assertEquals(cb1.value2.toString(), "12345678910111213141516178");
		} catch (Exception e) {
			assertFalse(true);
		}

		try {
			OptionsParser.ParseOption(new String[] {}, CheckIntegerFail.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Number is too big for integer. Found: 9000000000000000000000000");
		}
	}

	public static class Ignorable {
		int x;
		@Options(ignore = true)
		int y;
	}

	public void testIgnorable() {
		try {
			OptionsParser.ParseOption(new String[] { "--x", "100" }, Ignorable.class);
		} catch (Exception e) {
			fail("Should not be here");
		}

		try {
			OptionsParser.ParseOption(new String[] { "--y", "100" }, Ignorable.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "x should be single value, but set multiple values or does not exists");
		}
	}
}
