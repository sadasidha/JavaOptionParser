package com.simple.mind.optionreader;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.simple.mind.optionreader.annotations.Options;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class OptionParserTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public OptionParserTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(OptionParserTest.class);
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
			String[] args = new String[] { //
					"--file_names[", "file-one", "file-two", "]", //
					"--debug", //
					"--in", "10", //
					"--fileNames-3", "file-three", "file-four", "file-v", //
					"--debug2", //
					"--books", "blue", //
					"--b", "true" //
			}; //

			Arguments ar = OptionsParser.ParseOption(args, Arguments.class);
			assertEquals(ar.d2bug, true);
			assertEquals(ar.debug, true);
			assertEquals(ar.fileNames.size(), 5);
			assertEquals(ar.b.size(), 2);
			assertTrue(ar.b.contains("blue"));
			assertTrue(ar.b.contains("true"));

			Arguments ar2 = OptionsParser.ReadOptionNE(args, Arguments.class);
			assertEquals(ar2.d2bug, true);
			assertEquals(ar2.debug, true);
			assertEquals(ar2.fileNames.size(), 5);
			assertEquals(ar2.b.size(), 2);
			assertTrue(ar2.b.contains("blue"));
			assertTrue(ar2.b.contains("true"));
		} catch (Exception e) {
			fail("Should not be here");
		}

		Arguments arm = OptionsParser.ReadOptionNE(new String[] {}, Arguments.class);
		assertNull(arm);
	}

	public static class FailType1 {
		List<List<String>> ll;
	}

	public void testFailType1() {
		try {
			OptionsParser.ParseOption(new String[] { "--ll", "not important" }, FailType1.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(),
					"class sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl cannot be cast to class java.lang.Class (sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl and java.lang.Class are in module java.base of loader 'bootstrap')");
		}
	}

	public static class FailType2 {
		Map<String, String> ll;
	}

	public void testFailType2() {
		try {
			OptionsParser.ParseOption(new String[] { "--ll", "not important" }, FailType1.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(),
					"class sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl cannot be cast to class java.lang.Class (sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl and java.lang.Class are in module java.base of loader 'bootstrap')");
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
			e.printStackTrace();
			fail("Should not be here");
		}

		try {
			OptionsParser.ParseOption(new String[] { "--y", "100" }, Ignorable.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "x should be single value, but set multiple values or does not exists");
		}
	}

	public static class LongClass {
		List<Long> listObj;
		Long[] array;
		long[] arrayPri;
		long pri;
		Long ff;
	}

	public void testLognSetup() {
		try {
			LongClass lc = OptionsParser.ParseOption(new String[] { //
					"--list_Obj", "1000000", //
					"--array", "90000", //
					"--arrayPri", "100", //
					"--array_pri", "200", "--pri", "500", "--ff", "5000" //
			}, LongClass.class);
			assertEquals(lc.listObj.size(), 1);
			assertTrue(lc.listObj.get(0) == 1000000L);
			assertEquals(lc.array.length, 1);
			assertEquals(lc.arrayPri.length, 2);
			assertEquals(lc.arrayPri[0], 100);
			assertEquals(lc.arrayPri[1], 200);
			assertTrue(lc.array[0] == 90000L);
			assertTrue(lc.ff == 5000L);
			assertTrue(lc.pri == 500L);
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public static class StringClass {
		List<String> listString;
		String[] arrayString;
		String ff;
	}

	public void testStringSetup() {
		try {
			StringClass lc = OptionsParser.ParseOption(new String[] { //
					"--list_string", "1000000", //
					"--arrayString", "90000", //
					"--ff", "5000" //
			}, StringClass.class);
			assertEquals(lc.listString.size(), 1);
			assertEquals(lc.listString.get(0), "1000000");
			assertEquals(lc.arrayString.length, 1);
			assertEquals(lc.arrayString[0], "90000");
			assertEquals(lc.ff, "5000");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not be here");
		}
	}

	public static class BooleanClass {
		List<Boolean> list;
		Boolean[] array;
		Boolean ff;
		boolean[] pri;
		boolean df;
	}

	public void testBooleanSetup() {
		try {
			BooleanClass lc = OptionsParser.ParseOption(new String[] { //
					"--list", "true", //
					"--array", "false", //
					"--ff", //
					"--pri", "false", //
					"--df", "false" //
			}, BooleanClass.class);
			assertEquals(lc.list.size(), 1);
			assertTrue(lc.list.get(0));
			assertEquals(lc.array.length, 1);
			assertFalse(lc.array[0]);
			assertTrue(lc.ff);
			assertFalse(lc.pri[0]);
			assertFalse(lc.df);
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public static class BigIntClass {
		List<BigInteger> list;
		BigInteger[] array;
		BigInteger ff;
		BigInteger df;
	}

	public void testBigIntSetup() {
		try {
			BigIntClass lc = OptionsParser.ParseOption(new String[] { //
					"--list", "1234", //
					"--array", "567", //
					"--ff", "89", //
					"--df", "10" //
			}, BigIntClass.class);
			assertEquals(lc.list.size(), 1);
			assertTrue(lc.list.get(0).compareTo(BigInteger.valueOf(1234)) == 0);
			assertEquals(lc.array.length, 1);
			assertTrue(lc.array[0].compareTo(BigInteger.valueOf(567)) == 0);
			assertTrue(lc.ff.compareTo(BigInteger.valueOf(89)) == 0);
			assertTrue(lc.df.compareTo(BigInteger.valueOf(10)) == 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not be here");
		}
	}

	public static class IntegerClass {
		List<Integer> listObj;
		Integer[] array;
		int[] arrayPri;
		int pri;
		Integer ff;
	}

	public void testIntegerSetup() {
		try {
			IntegerClass lc = OptionsParser.ParseOption(new String[] { //
					"--list_Obj", "1000000", //
					"--array", "90000", //
					"--array_pri", "100", //
					"--array_pri", "-200", //
					"--pri", "500", //
					"--ff", "5000" //
			}, IntegerClass.class);
			assertEquals(lc.listObj.size(), 1);
			assertTrue(lc.listObj.get(0) == 1000000);
			assertEquals(lc.array.length, 1);
			assertEquals(lc.arrayPri.length, 2);
			assertEquals(lc.arrayPri[0], 100);
			assertEquals(lc.arrayPri[1], -200);
			assertTrue(lc.array[0] == 90000);
			assertTrue(lc.ff == 5000);
			assertTrue(lc.pri == 500);
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public static class ByteClass {
		List<Byte> listObj;
		Byte[] array;
		byte[] arrayPri;
		byte pri;
		Byte ff;
	}

	public void testByteSetup() {
		try {
			ByteClass lc = OptionsParser.ParseOption(new String[] { //
					"--listObj", "10", //
					"--array", "20", //
					"--array_pri", "30", //
					"--array_pri", "40", //
					"--pri", "50", //
					"--ff", "60" //
			}, ByteClass.class);
			assertEquals(lc.listObj.size(), 1);
			assertTrue(lc.listObj.get(0) == 10);
			assertEquals(lc.array.length, 1);
			assertTrue(lc.array[0] == 20);
			assertEquals(lc.arrayPri.length, 2);
			assertEquals(lc.arrayPri[0], 30);
			assertEquals(lc.arrayPri[1], 40);
			assertTrue(lc.pri == 50);
			assertTrue(lc.ff == 60);

		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public static class CharacterClass {
		List<Character> listObj;
		Character[] array;
		char[] arrayPri;
		Character pri;
		char ff;
	}

	public void testChareSetup() {
		try {
			CharacterClass lc = OptionsParser.ParseOption(new String[] { //
					"--listObj", "a", //
					"--array", "b", //
					"--array_pri", "c", //
					"--array_pri", "d", //
					"--pri", "e", //
					"--ff", "f" //
			}, CharacterClass.class);
			assertEquals(lc.listObj.size(), 1);
			assertTrue(lc.listObj.get(0) == 'a');
			assertEquals(lc.array.length, 1);
			assertTrue(lc.array[0] == 'b');
			assertEquals(lc.arrayPri.length, 2);
			assertEquals(lc.arrayPri[0], 'c');
			assertEquals(lc.arrayPri[1], 'd');
			assertTrue(lc.pri == 'e');
			assertTrue(lc.ff == 'f');

		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public static class DoubleClass {
		List<Double> listObj;
		Double[] array;
		double[] arrayPri;
		Double pri;
		double ff;
	}

	public void testDoubleSetup() {
		try {
			DoubleClass lc = OptionsParser.ParseOption(new String[] { //
					"--listObj", "1000.50", //
					"--array", "230000.50", //
					"--array_pri", "200", //
					"--array_pri", "900", //
					"--pri", "500.9", //
					"--ff", "800.3" //
			}, DoubleClass.class);
			assertEquals(lc.listObj.size(), 1);
			assertTrue(lc.listObj.get(0) == 1000.50);
			assertEquals(lc.array.length, 1);
			assertTrue(lc.array[0] == 230000.50);
			assertEquals(lc.arrayPri.length, 2);
			assertEquals(lc.arrayPri[0], 200.0);
			assertEquals(lc.arrayPri[1], 900.0);
			assertTrue(lc.pri == 500.9);
			assertTrue(lc.ff == 800.3);

		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public static class FloatClass {
		List<Float> listObj;
		Float[] array;
		float[] arrayPri;
		Float pri;
		float ff;
	}

	public void testFloatSetup() {
		try {
			FloatClass lc = OptionsParser.ParseOption(new String[] { //
					"--listObj", "1000.50", //
					"--array", "230000.50", //
					"--array_pri", "200", //
					"--array_pri", "900", //
					"--pri", "500.9", //
					"--ff", "800.3" //
			}, FloatClass.class);
			assertEquals(lc.listObj.size(), 1);
			assertTrue(lc.listObj.get(0) == 1000.50f);
			assertEquals(lc.array.length, 1);
			assertTrue(lc.array[0] == 230000.50);
			assertEquals(lc.arrayPri.length, 2);
			assertEquals(lc.arrayPri[0], 200f);
			assertEquals(lc.arrayPri[1], 900f);
			assertTrue(lc.pri == 500.9f);
			assertTrue(lc.ff == 800.3f);

		} catch (Exception e) {
			fail("Should not be here");
		}
	}

}
