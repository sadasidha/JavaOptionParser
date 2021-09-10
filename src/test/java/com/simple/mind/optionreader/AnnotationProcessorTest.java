package com.simple.mind.optionreader;

import java.lang.reflect.Field;
import java.util.List;

import com.simple.mind.optionreader.annotations.Options;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AnnotationProcessorTest extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AnnotationProcessorTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AnnotationProcessorTest.class);
	}

	public static class duplicatName1 {
		@Options(name = "x")
		String x;
	}

	public void testNameDuplicationTest() {
		try {
			OptionsParser.parse(new String[] {}, duplicatName1.class);
		} catch (Exception e) {

		}
	}

	public static class ClsToTest1 {
		@Options(defaultValues = { "true", "true" })
		String[] mult;
	}

	public static class ClsToTest2 {
		String[] bad_variable;
	}

	public void testDuplicateDefaultValue() {
		try {
			AnnotationProcessor.checkClassSanity(ClsToTest1.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Duplicates in default value: true");
		}

		try {
			AnnotationProcessor.checkClassSanity(ClsToTest2.class);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(),
					"This tool rejects snake style variable declaration. This is not ideal; but this seems safer. Rejected variable name: bad_variable");
		}
	}

	public static class T1 {
		int f;
		@Options(defaultValues = { "10" })
		int k;
		@Options(defaultValues = {})
		int j;
	}

	public void testHasDefaultValue() {
		try {
			assertFalse(AnnotationProcessor.hasDefaultValue(null));
		} catch (Exception e) {
		}

		try {
			assertFalse(AnnotationProcessor.hasDefaultValue(T1.class.getDeclaredField("f")));
		} catch (Exception e) {
			fail("Should not be here");
		}

		try {
			assertTrue(AnnotationProcessor.hasDefaultValue(T1.class.getDeclaredField("k")));
		} catch (Exception e) {
			fail("Should not be here");
		}

		try {
			assertFalse(AnnotationProcessor.hasDefaultValue(T1.class.getDeclaredField("j")));
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

	public static class ToFail {
		List<ClsToTest1> m;
	}

	public void testIsValidListGenericType() {
		try {
			AnnotationProcessor.isValidListGenericType(ToFail.class.getDeclaredField("m"));
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(),
					"Invalid list Type: com.simple.mind.optionreader.AnnotationProcessorTest$ClsToTest1");
		}
	}

	public static class ToFail2 {
		@Options(optional = true, defaultValues = "10")
		int abc;
		@Options(ignore = true, defaultValues = "10")
		int def;

		int name;
		@Options(name = "name")
		int notname;

	}

	public void testCheckClassSanityFields() {
		try {
			Field[] f = new Field[] { ToFail2.class.getDeclaredField("abc") };
			AnnotationProcessor.checkClassSanityFields(f);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(),
					"Making a variable optional and having default value is confusing. Class member: abc");
		}

		try {
			Field[] f = new Field[] { ToFail2.class.getDeclaredField("def") };
			AnnotationProcessor.checkClassSanityFields(f);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(),
					"Making a variable ignorable and having default value is confusing. Class member: def");
		}

		try {
			Field[] f = new Field[] { ToFail2.class.getDeclaredField("name"),
					ToFail2.class.getDeclaredField("notname") };
			AnnotationProcessor.checkClassSanityFields(f);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(),
					"Duplicate name: name; make sure member variable name does not conflict with other command option name");
		}

		try {
			Field[] f = new Field[] { ToFail2.class.getDeclaredField("notname"),
					ToFail2.class.getDeclaredField("name") };
			AnnotationProcessor.checkClassSanityFields(f);
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(),
					"Duplicate name: name; make sure member variable name does not conflict with other command option name");
		}
	}
}
