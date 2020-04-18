package com.simple.mind.optionreader;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
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
		@Options(name = { "books", "b" }, optional = false)
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
			Arguments ar = OptionParser.ParseOption(args, Arguments.class);
			assertEquals(ar.d2bug, true);
			assertEquals(ar.debug, true);
			assertEquals(ar.fileNames.size(), 5);
			assertEquals(ar.b.size(), 1);
			assertEquals(ar.b.get(0), "blue");
		} catch (Exception e) {
			assertFalse(true);
		}
	}

	public void testArgumentProcessing() throws Throwable {
		HashMap<String, ArrayList<String>> l = Parser.processArgs(new String[] { "--ff", "Coon", "--ff", "spoon" });
		assertEquals(l.size(), 1);
		assertEquals(l.get("ff").size(), 2);
		assertTrue(l.get("ff").contains("Coon"));
		assertTrue(l.get("ff").contains("spoon"));

		l = Parser.processArgs(new String[] { "--ff", "Coon", "--debug", "--ff", "spoon" });
		assertEquals(l.size(), 2);
		assertEquals(l.get("ff").size(), 2);
		assertTrue(l.get("ff").contains("Coon"));
		assertTrue(l.get("ff").contains("spoon"));
		assertTrue(l.get("debug").contains("true"));

		l = Parser.processArgs(new String[] { "--ff", "Coon", "--debug", "false", "--ff", "spoon" });
		assertEquals(l.size(), 2);
		assertEquals(l.get("ff").size(), 2);
		assertTrue(l.get("ff").contains("Coon"));
		assertTrue(l.get("ff").contains("spoon"));
		assertTrue(l.get("debug").contains("false"));

		l = Parser.processArgs(new String[] { "--ff", "Coon", "--debug", "false", "--ff", "spoon" });
		assertEquals(l.size(), 2);
		assertEquals(l.get("ff").size(), 2);
		assertTrue(l.get("ff").contains("Coon"));
		assertTrue(l.get("ff").contains("spoon"));
		assertTrue(l.get("debug").contains("false"));

		l = Parser.processArgs(new String[] { "--ff", "Coon", "--debug", "false", "--ff", "spoon", "--ff-3", "broom",
				"Doom", "Room" });
		assertEquals(l.size(), 2);
		assertEquals(l.get("ff").size(), 5);
		assertTrue(l.get("ff").contains("Coon"));
		assertTrue(l.get("ff").contains("spoon"));
		assertTrue(l.get("ff").contains("broom"));
		assertTrue(l.get("ff").contains("Doom"));
		assertTrue(l.get("ff").contains("Room"));
		assertTrue(l.get("debug").contains("false"));

		l = Parser.processArgs(new String[] { "--ff", "coon", "--debug", "false", "--ff", "spoon", "--ff-3", "broom",
				"doom", "room", "--ff[", "kaboom", "]" });
		assertEquals(l.size(), 2);
		assertEquals(l.get("ff").size(), 6);
		assertTrue(l.get("ff").contains("coon"));
		assertTrue(l.get("ff").contains("spoon"));
		assertTrue(l.get("ff").contains("broom"));
		assertTrue(l.get("ff").contains("doom"));
		assertTrue(l.get("ff").contains("room"));
		assertTrue(l.get("ff").contains("kaboom"));
		assertTrue(l.get("debug").contains("false"));
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

	public static class ClsToTest3 {
		@Options(defaultValues = { "true", "true" })
		String[] mult;
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
			OptionParser.ParseOption(new String[] {}, NodefToFail.class);
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "debug should be single value, but set multiple values or does not exists");
		}
		try {
			OptionParser.ParseOption(new String[] { "--abc def" }, ClsToTest1.class);
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(e.getMessage(),
					"Parameter name should not contain any non alpha numeric or non hyphen or non underscore characetr. Found: --abc def");
		}
		try {
			ClsToTest1 clsToTest1_1 = null;
			clsToTest1_1 = OptionParser.ParseOption(new String[] {}, ClsToTest1.class);
			assertTrue(clsToTest1_1.debug);
			clsToTest1_1 = OptionParser.ParseOption(new String[] { "--debug", "false" }, ClsToTest1.class);
			assertFalse(clsToTest1_1.debug);
			clsToTest1_1 = OptionParser.ParseOption(new String[] { "--debug", "true" }, ClsToTest1.class);
			assertTrue(clsToTest1_1.debug);
		} catch (Exception e) {
			assertFalse(true);
		}
		try {
			OptionParser.ParseOption(new String[] { "--debug", "FLUE" }, ClsToTest1.class);
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid Boolean value. Found: FLUE");
		}

		try {
			OptionParser.ParseOption(new String[] {}, ClsToTest2.class);
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Expecting Single value, received multiple in default");
		}
		try {
			OptionParser.ParseOption(new String[] {}, ClsToTest3.class);
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Duplicates in default value: true");
		}

		ClsToTest4 cls4 = null;
		try {
			cls4 = OptionParser.ParseOption(new String[] { "--mult1", "--mult1", "--mult2", "--mult2", "true",
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
			assertFalse(true);
		}

		try {
			cls4 = OptionParser.ParseOption(
					new String[] { "--mult1", "--mult1", "--mult2", "--mult2", "true", "--mult2", "false", "--mult3" },
					ClsToTest4.class);
			assertFalse(true);
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
			OptionParser.ParseOption(new String[] { "--value2" }, CheckBigInt.class);
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid Number is passed. Found: true");
		}

		try {
			OptionParser.ParseOption(new String[] { "--value2-2", "20", "90" }, CheckBigInt.class);
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "value2 should be single value, but set multiple values or does not exists");
		}

		try {
			CheckBigInt cb1 = OptionParser.ParseOption(
					new String[] { "--value1-2", "20", "90", "--value2", "12345678910111213141516178" },
					CheckBigInt.class);
			assertEquals(cb1.value1[0], BigInteger.valueOf(20));
			assertEquals(cb1.value1[1], BigInteger.valueOf(90));
			assertEquals(cb1.value2.toString(), "12345678910111213141516178");
		} catch (Exception e) {
			assertFalse(true);
		}

		try {
			OptionParser.ParseOption(new String[] {}, CheckIntegerFail.class);
			assertFalse(true);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Number is too big for integer. Found: 9000000000000000000000000");
		}

	}
}
