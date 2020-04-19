package com.simple.mind.optionreader;

import com.simple.mind.optionreader.StringProcessor.NamgeLength;

import junit.framework.TestCase;

public class StringProcessorTest extends TestCase {

	public StringProcessorTest(String testName) {
		super(testName);
	}

	public void testFromSnake() {
		assertEquals(StringProcessor.convertFromSnake("corona_virus"), "coronaVirus");
		assertEquals(StringProcessor.convertFromSnake("corona__virus"), "corona_Virus");
		assertEquals(StringProcessor.convertFromSnake("corona___virus"), "corona__Virus");
		assertEquals(StringProcessor.convertFromSnake("corona_3_virus"), "corona3Virus");
	}

	public void testGetLengthAndName() {
		try {
			NamgeLength f = StringProcessor.getLengthAndName("--a-3");
			assertTrue(f.length == 3);
			assertEquals(f.name, "--a");
			f = StringProcessor.getLengthAndName("--a_b_c-10");
			assertTrue(f.length == 10);
			assertEquals(f.name, "--a_b_c");
		} catch (Exception e) {
			fail("Should not be here");
		}
	}
}
