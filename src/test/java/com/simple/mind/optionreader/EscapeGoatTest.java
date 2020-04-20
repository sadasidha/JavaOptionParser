package com.simple.mind.optionreader;

import com.simple.mind.optionreader.example.Example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class EscapeGoatTest extends TestCase {
	public EscapeGoatTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(EscapeGoatTest.class);
	}

	public static class SimpleClass {

	}

	public void testEscape() {
		new ParseValueToPrimary();
		new AcceptableList();
		new OptionsParser<String>();
		new AnnotationProcessor();
		new Example();
		try {
			AnnotationProcessor.checkClassSanity(SimpleClass.class);
		} catch (Exception e1) {
			fail("Should not be here");
		}

		try {
			Example.main(new String[] { "--fileNames", "name.json" });
		} catch (Exception e) {
			fail("Should not be here");
		}
	}

}
