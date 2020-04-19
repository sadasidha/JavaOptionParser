package com.simple.mind.optionreader;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ArgumentProcessingTest extends TestCase {
	public ArgumentProcessingTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public void testArgumentProcessing() throws Throwable {
		HashMap<String, ArrayList<String>> l = CommandParser
				.processArgs(new String[] { "--ff", "Coon", "--ff", "spoon" });
		assertEquals(l.size(), 1);
		assertEquals(l.get("ff").size(), 2);
		assertTrue(l.get("ff").contains("Coon"));
		assertTrue(l.get("ff").contains("spoon"));

		l = CommandParser.processArgs(new String[] { "--ff", "Coon", "--debug", "--ff", "spoon" });
		assertEquals(l.size(), 2);
		assertEquals(l.get("ff").size(), 2);
		assertTrue(l.get("ff").contains("Coon"));
		assertTrue(l.get("ff").contains("spoon"));
		assertTrue(l.get("debug").contains("true"));

		l = CommandParser.processArgs(new String[] { "--ff", "Coon", "--debug", "false", "--ff", "spoon" });
		assertEquals(l.size(), 2);
		assertEquals(l.get("ff").size(), 2);
		assertTrue(l.get("ff").contains("Coon"));
		assertTrue(l.get("ff").contains("spoon"));
		assertTrue(l.get("debug").contains("false"));

		l = CommandParser.processArgs(new String[] { "--ff", "Coon", "--debug", "false", "--ff", "spoon" });
		assertEquals(l.size(), 2);
		assertEquals(l.get("ff").size(), 2);
		assertTrue(l.get("ff").contains("Coon"));
		assertTrue(l.get("ff").contains("spoon"));
		assertTrue(l.get("debug").contains("false"));

		l = CommandParser.processArgs(new String[] { "--ff", "Coon", "--debug", "false", "--ff", "spoon", "--ff-3",
				"broom", "Doom", "Room" });
		assertEquals(l.size(), 2);
		assertEquals(l.get("ff").size(), 5);
		assertTrue(l.get("ff").contains("Coon"));
		assertTrue(l.get("ff").contains("spoon"));
		assertTrue(l.get("ff").contains("broom"));
		assertTrue(l.get("ff").contains("Doom"));
		assertTrue(l.get("ff").contains("Room"));
		assertTrue(l.get("debug").contains("false"));

		l = CommandParser.processArgs(new String[] { "--ff", "coon", "--debug", "false", "--ff", "spoon", "--ff-3",
				"broom", "doom", "room", "--ff[", "kaboom", "]" });
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
}
