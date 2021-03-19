package com.simple.mind.optionreader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ParserCommandTest extends TestCase {
    public ParserCommandTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ParserCommandTest.class);
    }

    public void testRepeateParameterWithValue() {

        ParseCommand l = new ParseCommand(new String[] { "--ff", "Coon", "--ff", "spoon" });
        assertEquals(l.getOptionCount(), 1);
        assertEquals(l.getList("ff").size(), 2);
        assertTrue(l.getList("ff").contains("Coon"));
        assertTrue(l.getList("ff").contains("spoon"));

    }

    public void testOptionWithAndWiouthValue() {
        ParseCommand l = new ParseCommand(new String[] { "--ff", "Coon", "--debug", "--ff", "spoon" });
        assertEquals(l.getOptionCount(), 2);
        assertEquals(l.getList("ff").size(), 2);
        assertTrue(l.getList("ff").contains("Coon"));
        assertTrue(l.getList("ff").contains("spoon"));
        assertTrue(l.getList("debug").contains("true"));

    }

    public void testMultipleParameterWithValue() {
        ParseCommand l = new ParseCommand(new String[] { "--ff", "Coon", "--debug", "false", "--ff", "spoon" });
        assertEquals(l.getOptionCount(), 2);
        assertEquals(l.getList("ff").size(), 2);
        assertTrue(l.getList("ff").contains("Coon"));
        assertTrue(l.getList("ff").contains("spoon"));
        assertEquals(l.getList("debug").size(), 1);
        assertTrue(l.getList("debug").contains("false"));
    }

    public void testMultiplePramterWithMultipleValueAppendingIntoSingleWithRange() {
        ParseCommand l = new ParseCommand(new String[] { "--ff", "Coon", //
                "--debug", "false", //
                "--ff", "spoon", //
                "--ff", "broom", "Doom", "Room" //
        });
        assertEquals(l.getOptionCount(), 2);
        assertEquals(l.getList("ff").size(), 5);
        assertTrue(l.getList("ff").contains("Coon"));
        assertTrue(l.getList("ff").contains("spoon"));
        assertTrue(l.getList("ff").contains("broom"));
        assertTrue(l.getList("ff").contains("Doom"));
        assertTrue(l.getList("ff").contains("Room"));
        assertTrue(l.getList("debug").contains("false"));

    }

    public void testMultipleParamterWithRangeAndBracket() {
        ParseCommand l = new ParseCommand(new String[] { "--ff", "coon", //
                "--debug", "false", //
                "--ff", "spoon", //
                "--ff", "broom", "doom", "room", //
                "--ff", "kaboom", "" //
        });
        assertEquals(l.getOptionCount(), 2);
        assertEquals(l.getList("ff").size(), 7);
        assertTrue(l.getList("ff").contains("coon"));
        assertTrue(l.getList("ff").contains("spoon"));
        assertTrue(l.getList("ff").contains("broom"));
        assertTrue(l.getList("ff").contains("doom"));
        assertTrue(l.getList("ff").contains("room"));
        assertTrue(l.getList("ff").contains("kaboom"));
        assertTrue(l.getList("ff").contains(""));
        assertTrue(l.getList("debug").contains("false"));

    }

    public void testAllCases() {

    }
}
