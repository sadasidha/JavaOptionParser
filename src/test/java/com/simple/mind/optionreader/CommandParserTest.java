package com.simple.mind.optionreader;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CommandParserTest extends TestCase {
	public CommandParserTest(String testName) {
		super(testName);
		new CommandParser();
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(CommandParserTest.class);
	}

	public void testUnnecessaryCommand() {
		try {
			CommandParser.processArgs(new String[] { "--ff", "Coon", "spoon" });
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid option: spoon");
		}

		try {
			CommandParser.processArgs(new String[] { "Coon", "spoon" });
			fail("Should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid option: Coon");
		}
	}

	public void testRepeateParameterWithValue() {
		try {
			HashMap<String, ArrayList<String>> l = CommandParser
					.processArgs(new String[] { "--ff", "Coon", "--ff", "spoon" });
			assertEquals(l.size(), 1);
			assertEquals(l.get("ff").size(), 2);
			assertTrue(l.get("ff").contains("Coon"));
			assertTrue(l.get("ff").contains("spoon"));
		} catch (Exception e) {
			fail("It should not be here");
		}
	}

	public void testOptionWithAndWiouthValue() {
		try {
			HashMap<String, ArrayList<String>> l;
			l = CommandParser.processArgs(new String[] { "--ff", "Coon", "--debug", "--ff", "spoon" });
			assertEquals(l.size(), 2);
			assertEquals(l.get("ff").size(), 2);
			assertTrue(l.get("ff").contains("Coon"));
			assertTrue(l.get("ff").contains("spoon"));
			assertTrue(l.get("debug").contains("true"));
		} catch (Exception e) {
			fail("It should not be here");
		}
	}

	public void testMultipleParameterWithValue() {
		try {
			HashMap<String, ArrayList<String>> l;
			l = CommandParser.processArgs(new String[] { "--ff", "Coon", "--debug", "false", "--ff", "spoon" });
			assertEquals(l.size(), 2);
			assertEquals(l.get("ff").size(), 2);
			assertTrue(l.get("ff").contains("Coon"));
			assertTrue(l.get("ff").contains("spoon"));
			assertEquals(l.get("debug").size(), 1);
			assertTrue(l.get("debug").contains("false"));
		} catch (Exception e) {
			fail("It should not be here");
		}
	}

	public void testMultiplePramterWithMultipleValueAppendingIntoSingleWithRange() {
		try {
			HashMap<String, ArrayList<String>> l;
			l = CommandParser.processArgs(new String[] { "--ff", "Coon", //
					"--debug", "false", //
					"--ff", "spoon", //
					"--ff-3", "broom", "Doom", "Room" //
			});
			assertEquals(l.size(), 2);
			assertEquals(l.get("ff").size(), 5);
			assertTrue(l.get("ff").contains("Coon"));
			assertTrue(l.get("ff").contains("spoon"));
			assertTrue(l.get("ff").contains("broom"));
			assertTrue(l.get("ff").contains("Doom"));
			assertTrue(l.get("ff").contains("Room"));
			assertTrue(l.get("debug").contains("false"));
		} catch (Exception e) {

		}
	}

	public void testMultipleParamterWithRangeAndBracket() {
		try {
			HashMap<String, ArrayList<String>> l;
			l = CommandParser.processArgs(new String[] { "--ff", "coon", //
					"--debug", "false", //
					"--ff", "spoon", //
					"--ff-3", "broom", "doom", "room", //
					"--ff[", "kaboom", "]" //
			});
			assertEquals(l.size(), 2);
			assertEquals(l.get("ff").size(), 6);
			assertTrue(l.get("ff").contains("coon"));
			assertTrue(l.get("ff").contains("spoon"));
			assertTrue(l.get("ff").contains("broom"));
			assertTrue(l.get("ff").contains("doom"));
			assertTrue(l.get("ff").contains("room"));
			assertTrue(l.get("ff").contains("kaboom"));
			assertTrue(l.get("debug").contains("false"));
		} catch (Exception e) {
			fail("This should not be here");
		}
	}

	public void testBadRange1() {
		try {
			CommandParser.processArgs(new String[] { //
					"--ff-3", "broom", "doom", //
			});
			fail("This should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Number of value does not match for ff; expected 3 items");
		}
	}

	public void testBadRange2() {
		try {
			CommandParser.processArgs(new String[] { //
					"--ff[", "broom", "doom", //
			});
			fail("This should not be here");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid input. Range ended unexpectedly");
		}
	}
}
