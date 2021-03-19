package com.simple.mind.optionreader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StringProcessorTest extends TestCase {

    public StringProcessorTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(StringProcessorTest.class);
    }

    public void testFromSnake() {
        assertEquals(StringFormatter.formatName("ab_cd"), "abCd");
        assertEquals(StringFormatter.formatName("abCd"), "abCd");
        assertEquals(StringFormatter.formatName("---corona_3_virus"), "-corona3Virus");
    }

}
