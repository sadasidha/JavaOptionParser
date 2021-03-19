package com.simple.mind.optionreader;

import java.math.BigInteger;
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
        @Options(name = "books", optional = false)
        public List<String> b;
        public List<String> fileNames;
        public boolean debug;
        @Options(name = "debug2")
        public boolean d2bug;
        public Integer in;
        @Options(ignore = true)
        public static boolean ignore;
    }

    public void testValidArgs() {
        String[] args = new String[] { //
                "--file_names", "file-one", "file-two", //
                "--debug", //
                "--in", "10", //
                "--file_names", "file-three", "file-four", "file-v", //
                "--debug2", //
                "--books", "blue", //
                "--books", "true" //
        }; //

        Arguments ar = OptionsParser.ParseOption(args, Arguments.class);
        assertEquals(ar.d2bug, true);
        assertEquals(ar.debug, true);
        assertEquals(ar.fileNames.size(), 5);
        assertEquals(ar.b.size(), 2);
        assertTrue(ar.b.contains("blue"));
        assertTrue(ar.b.contains("true"));
    }

    public static class FailType1 {
        List<List<String>> ll;
    }

    public void testFailType1() {
        try {
            OptionsParser.ParseOption(new String[] { "--ll", "not important" }, FailType1.class);
            fail("Should not be here");
        } catch (Exception e) {
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
        }
    }

    public static class ClsToTest1 {
        @Options(defaultValues = "true")
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
            OptionsParser.ParseOption(new String[] { "--abc def" }, ClsToTest1.class);
            fail("Should not be here");
        } catch (Exception e) {
        }

        ClsToTest1 clsToTest1_1 = null;
        clsToTest1_1 = OptionsParser.ParseOption(new String[] {}, ClsToTest1.class);
        assertTrue(clsToTest1_1.debug);
        clsToTest1_1 = OptionsParser.ParseOption(new String[] { "--debug", "false" }, ClsToTest1.class);
        assertFalse(clsToTest1_1.debug);
        clsToTest1_1 = OptionsParser.ParseOption(new String[] { "--debug", "true" }, ClsToTest1.class);
        assertTrue(clsToTest1_1.debug);

        try {
            OptionsParser.ParseOption(new String[] { "--debug", "FLUE" }, ClsToTest1.class);
            fail("Should not be here");
        } catch (Exception e) {

        }

        try {
            OptionsParser.ParseOption(new String[] {}, ClsToTest2.class);
            fail("Should not be here");
        } catch (Exception e) {

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

        }

        try {
            OptionsParser.ParseOption(new String[] { "--value2", "20", "90" }, CheckBigInt.class);
            fail("Should not be here");
        } catch (Exception e) {
        }

        CheckBigInt cb1 = OptionsParser.ParseOption(
                new String[] { "--value1", "20", "90", "--value2", "12345678910111213141516178" }, CheckBigInt.class);
        assertEquals(cb1.value1[0], BigInteger.valueOf(20));
        assertEquals(cb1.value1[1], BigInteger.valueOf(90));
        assertEquals(cb1.value2.toString(), "12345678910111213141516178");

        try {
            OptionsParser.ParseOption(new String[] {}, CheckIntegerFail.class);
            fail("Should not be here");
        } catch (Exception e) {

        }
    }

    public static class Ignorable {
        int x;
        @Options(ignore = true)
        int y;
    }

    public void testIgnorable() {

        OptionsParser.ParseOption(new String[] { "--x", "100" }, Ignorable.class);

        try {
            OptionsParser.ParseOption(new String[] { "--y", "100" }, Ignorable.class);
            fail("Should not be here");
        } catch (Exception e) {
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

    }

    public static class StringClass {
        List<String> listString;
        String[] arrayString;
        String ff;
    }

    public void testStringSetup() {

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

    }

    public static class BooleanClass {
        List<Boolean> list;
        Boolean[] array;
        Boolean ff;
        boolean[] pri;
        boolean df;
    }

    public void testBooleanSetup() {

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

    }

    public static class BigIntClass {
        List<BigInteger> list;
        BigInteger[] array;
        BigInteger ff;
        BigInteger df;
    }

    public void testBigIntSetup() {

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

    }

    public static class IntegerClass {
        List<Integer> listObj;
        Integer[] array;
        int[] arrayPri;
        int pri;
        Integer ff;
    }

    public void testIntegerSetup() {

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

    }

    public static class ByteClass {
        List<Byte> listObj;
        Byte[] array;
        byte[] arrayPri;
        byte pri;
        Byte ff;
    }

    public void testByteSetup() {

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
    }

    public static class FloatClass {
        List<Float> listObj;
        Float[] array;
        float[] arrayPri;
        Float pri;
        float ff;
    }

    public void testFloatSetup() {
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
    }

    public static class AllPrimary {
        int value01;
        long value02;
        byte value03;
        char value04;
        float value05;
        double value06;
        boolean value07;
        String value08;
    }

    public void testAllTypes() {
        AllPrimary lc = OptionsParser.ParseOption(new String[] { //
                "--value01", "101", //
                "--value02", "102", //
                "--value03", "103", //
                "--value04", "4", //
                "--value05", "105", //
                "--value06", "106", //
                "--value07", //
                "--value08", "108" //
        }, AllPrimary.class);
        assertEquals(lc.value01, 101);
        assertEquals(lc.value02, 102);
        assertEquals(lc.value03, 103);
        assertEquals(lc.value04, '4');
        assertEquals(lc.value05, 105.0f);
        assertEquals(lc.value06, 106.0d);
        assertEquals(lc.value07, true);
        assertEquals(lc.value08, "108");
    }

    public static class AllPrimaryArray {

        int[] value01;
        long[] value02;
        byte[] value03;
        char[] value04;
        float[] value05;
        double[] value06;
        boolean[] value07;
        String[] value08;
    }

    public void testAllPrimaryArray() {
        //@formatter:off
        AllPrimaryArray lc = OptionsParser.ParseOption(new String[] { //
                "--value01", "101", "111", //
                "--value02", "102", "112",    "122", //
                "--value03", "103", "113",    "123", "127", //
                "--value04", "4",   "5",      "6",   "7",      "8", //
                "--value05", "105", "115",    "125", "135",    "145", "155", //
                "--value06", "106", "116",    "126", "136",    "146", "156",   "166", //
                "--value07", "true", "false", "True", "False", "tRue", "fAlse", "trUe", "falSe", //
                "--value08", "108",  "118",   "128",  "138",   "148",  "158",   "168",  "178",   "188" //
        }, AllPrimaryArray.class);
        //@formatter:on

        assertEquals(lc.value01.length, 2);
        assertEquals(lc.value02.length, 3);
        assertEquals(lc.value03.length, 4);
        assertEquals(lc.value04.length, 5);
        assertEquals(lc.value05.length, 6);
        assertEquals(lc.value06.length, 7);
        assertEquals(lc.value07.length, 8);
        assertEquals(lc.value08.length, 9);
        int[] c1 = new int[] { 101, 111 };
        for (int i = 0; i < c1.length; i++) {
            assertEquals(lc.value01[i], c1[i]);
        }

        long[] c2 = new long[] { 102, 112, 122 };
        for (int i = 0; i < c2.length; i++) {
            assertEquals(lc.value02[i], c2[i]);
        }

        byte[] c3 = new byte[] { 103, 113, 123, 127 };
        for (int i = 0; i < c3.length; i++) {
            assertEquals(lc.value03[i], c3[i]);
        }

        char[] c4 = new char[] { '4', '5', '6', '7', '8' };
        for (int i = 0; i < c4.length; i++) {
            assertEquals(lc.value04[i], c4[i]);
        }

        float[] c5 = new float[] { 105.0f, 115.0f, 125.0f, 135.0f, 145.0f, 155.0f };
        for (int i = 0; i < c5.length; i++) {
            assertEquals(lc.value05[i], c5[i]);
        }

        double[] c6 = new double[] { 106.0d, 116.0d, 126.0d, 136.0d, 146.0d, 156.0d, 166.0d };
        for (int i = 0; i < c6.length; i++) {
            assertEquals(lc.value06[i], c6[i]);
        }

        boolean[] c7 = new boolean[] { true, false, true, false, true, false, true, false };
        for (int i = 0; i < c7.length; i++) {
            assertEquals(lc.value07[i], c7[i]);
        }

        String[] c8 = new String[] { "108", "118", "128", "138", "148", "158", "168", "178", "188" };
        for (int i = 0; i < c8.length; i++) {
            assertEquals(lc.value08[i], c8[i]);
        }
    }

    public static class AllPrimaryObject {
        Integer value01;
        Long value02;
        Byte value03;
        Character value04;
        Float value05;
        Double value06;
        Boolean value07;
        BigInteger value08;
    }

    public void testAllPrimaryObject() {
        AllPrimaryObject lc = OptionsParser.ParseOption(new String[] { //
                "--value01", "101", //
                "--value02", "102", //
                "--value03", "103", //
                "--value04", "4", //
                "--value05", "105", //
                "--value06", "106", //
                "--value07", //
                "--value08", "108" //
        }, AllPrimaryObject.class);
        assertEquals((int) lc.value01, 101);
        assertEquals((long) lc.value02, 102);
        assertEquals((byte) lc.value03, 103);
        assertEquals((char) lc.value04, '4');
        assertEquals(lc.value05, 105.0f);
        assertEquals(lc.value06, 106.0d);
        assertEquals((boolean) lc.value07, true);
        assertEquals(lc.value08, new BigInteger("108"));
    }

    public static class AllPrimaryObjectArray {
        Integer[] value01;
        Long[] value02;
        Byte[] value03;
        Character[] value04;
        Float[] value05;
        Double[] value06;
        Boolean[] value07;
        String[] value08;
        BigInteger[] value09;
    }

    public void testAllPrimaryObjectArray() {
        //@formatter:off
        AllPrimaryObjectArray lc = OptionsParser.ParseOption(new String[] { //
                "--value01", "101", "111", //
                "--value02", "102", "112",    "122", //
                "--value03", "103", "113",    "123", "127", //
                "--value04", "4",   "5",      "6",   "7",      "8", //
                "--value05", "105", "115",    "125", "135",    "145", "155", //
                "--value06", "106", "116",    "126", "136",    "146", "156",   "166", //
                "--value07", "true", "false", "True", "False", "tRue", "fAlse", "trUe", "falSe", //
                "--value08", "108",  "118",   "128",  "138",   "148",  "158",   "168",  "178",   "188", //
                "--value09", "109",  "119",   "129",  "139",   "149",  "159",   "169",  "179",   "189", "199" //
        }, AllPrimaryObjectArray.class);
        //@formatter:on

        assertEquals(lc.value01.length, 2);
        assertEquals(lc.value02.length, 3);
        assertEquals(lc.value03.length, 4);
        assertEquals(lc.value04.length, 5);
        assertEquals(lc.value05.length, 6);
        assertEquals(lc.value06.length, 7);
        assertEquals(lc.value07.length, 8);
        assertEquals(lc.value08.length, 9);
        assertEquals(lc.value09.length, 10);
        Integer[] c1 = new Integer[] { 101, 111 };
        for (int i = 0; i < c1.length; i++) {
            assertEquals(lc.value01[i], c1[i]);
        }

        Long[] c2 = new Long[] { 102l, 112l, 122l };
        for (int i = 0; i < c2.length; i++) {
            assertEquals(lc.value02[i], c2[i]);
        }

        Byte[] c3 = new Byte[] { 103, 113, 123, 127 };
        for (int i = 0; i < c3.length; i++) {
            assertEquals(lc.value03[i], c3[i]);
        }

        Character[] c4 = new Character[] { '4', '5', '6', '7', '8' };
        for (int i = 0; i < c4.length; i++) {
            assertEquals(lc.value04[i], c4[i]);
        }

        Float[] c5 = new Float[] { 105.0f, 115.0f, 125.0f, 135.0f, 145.0f, 155.0f };
        for (int i = 0; i < c5.length; i++) {
            assertEquals(lc.value05[i], c5[i]);
        }

        Double[] c6 = new Double[] { 106.0d, 116.0d, 126.0d, 136.0d, 146.0d, 156.0d, 166.0d };
        for (int i = 0; i < c6.length; i++) {
            assertEquals(lc.value06[i], c6[i]);
        }

        Boolean[] c7 = new Boolean[] { true, false, true, false, true, false, true, false };
        for (int i = 0; i < c7.length; i++) {
            assertEquals(lc.value07[i], c7[i]);
        }

        String[] c8 = new String[] { "108", "118", "128", "138", "148", "158", "168", "178", "188" };
        for (int i = 0; i < c8.length; i++) {
            assertEquals(lc.value08[i], c8[i]);
        }
        BigInteger[] c9 = new BigInteger[] { new BigInteger("109"), new BigInteger("119"), new BigInteger("129"),
                new BigInteger("139"), new BigInteger("149"), new BigInteger("159"), new BigInteger("169"),
                new BigInteger("179"), new BigInteger("189") };
        for (int i = 0; i < c9.length; i++) {
            assertEquals(lc.value09[i], c9[i]);
        }
    }

    public static class AllList {
        List<Integer> value01;
        List<Long> value02;
        List<Byte> value03;
        List<Character> value04;
        List<Float> value05;
        List<Double> value06;
        List<Boolean> value07;
        List<String> value08;
        List<BigInteger> value09;
    }

    public void testAllList() {
        //@formatter:off
        AllList lc = OptionsParser.ParseOption(new String[] { //
                "--value01", "101", "111", //
                "--value02", "102", "112",    "122", //
                "--value03", "103", "113",    "123", "127", //
                "--value04", "4",   "5",      "6",   "7",      "8", //
                "--value05", "105", "115",    "125", "135",    "145", "155", //
                "--value06", "106", "116",    "126", "136",    "146", "156",   "166", //
                "--value07", "true", "false", "True", "False", "tRue", "fAlse", "trUe", "falSe", //
                "--value08", "108",  "118",   "128",  "138",   "148",  "158",   "168",  "178",   "188", //
                "--value09", "109",  "119",   "129",  "139",   "149",  "159",   "169",  "179",   "189", "199" //
        }, AllList.class);
        //@formatter:on

        assertEquals(lc.value01.size(), 2);
        assertEquals(lc.value02.size(), 3);
        assertEquals(lc.value03.size(), 4);
        assertEquals(lc.value04.size(), 5);
        assertEquals(lc.value05.size(), 6);
        assertEquals(lc.value06.size(), 7);
        assertEquals(lc.value07.size(), 8);
        assertEquals(lc.value08.size(), 9);
        assertEquals(lc.value09.size(), 10);
        Integer[] c1 = new Integer[] { 101, 111 };
        for (int i = 0; i < c1.length; i++) {
            assertEquals(lc.value01.get(i), c1[i]);
        }

        Long[] c2 = new Long[] { 102l, 112l, 122l };
        for (int i = 0; i < c2.length; i++) {
            assertEquals(lc.value02.get(i), c2[i]);
        }

        Byte[] c3 = new Byte[] { 103, 113, 123, 127 };
        for (int i = 0; i < c3.length; i++) {
            assertEquals(lc.value03.get(i), c3[i]);
        }

        Character[] c4 = new Character[] { '4', '5', '6', '7', '8' };
        for (int i = 0; i < c4.length; i++) {
            assertEquals(lc.value04.get(i), c4[i]);
        }

        Float[] c5 = new Float[] { 105.0f, 115.0f, 125.0f, 135.0f, 145.0f, 155.0f };
        for (int i = 0; i < c5.length; i++) {
            assertEquals(lc.value05.get(i), c5[i]);
        }

        Double[] c6 = new Double[] { 106.0d, 116.0d, 126.0d, 136.0d, 146.0d, 156.0d, 166.0d };
        for (int i = 0; i < c6.length; i++) {
            assertEquals(lc.value06.get(i), c6[i]);
        }

        Boolean[] c7 = new Boolean[] { true, false, true, false, true, false, true, false };
        for (int i = 0; i < c7.length; i++) {
            assertEquals(lc.value07.get(i), c7[i]);
        }

        String[] c8 = new String[] { "108", "118", "128", "138", "148", "158", "168", "178", "188" };
        for (int i = 0; i < c8.length; i++) {
            assertEquals(lc.value08.get(i), c8[i]);
        }
        BigInteger[] c9 = new BigInteger[] { new BigInteger("109"), new BigInteger("119"), new BigInteger("129"),
                new BigInteger("139"), new BigInteger("149"), new BigInteger("159"), new BigInteger("169"),
                new BigInteger("179"), new BigInteger("189") };
        for (int i = 0; i < c9.length; i++) {
            assertEquals(lc.value09.get(i), c9[i]);
        }
    }

}
