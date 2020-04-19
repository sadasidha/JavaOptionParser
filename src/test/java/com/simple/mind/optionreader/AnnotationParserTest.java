package com.simple.mind.optionreader;

import com.simple.mind.optionreader.annotations.Options;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AnnotationParserTest extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AnnotationParserTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public static class duplicatName1 {
		@Options(name = "x")
		String x;
	}

	public void testNameDuplicationTest() {
		try {
			OptionsParser.ParseOption(new String[] {}, duplicatName1.class);
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

}
