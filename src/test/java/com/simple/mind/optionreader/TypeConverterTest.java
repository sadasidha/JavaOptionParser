
package com.simple.mind.optionreader;

import java.math.BigInteger;

import junit.framework.TestCase;

public class TypeConverterTest extends TestCase {

    public void testToBigInt() {
        try {
            TypeConverter.toBigInt("abc");
            fail("Should not be here");
        } catch (Exception e) {

        }

        try {
            assertEquals(
                    TypeConverter.toBigInt("99559955995599559955").compareTo(new BigInteger("99559955995599559955")),
                    0);
        } catch (Exception e) {
            fail("Should not be here");
        }
    }

    public void testToInt() {
        try {
            TypeConverter.toInt("abc");
            fail("Should not be here");
        } catch (Exception e) {

        }

        try {
            TypeConverter.toInt("99559955995599559955");
            fail("Should not be here");
        } catch (Exception e) {
        }

        try {
            TypeConverter.toInt("-99559955995599559955");
            fail("Should not be here");
        } catch (Exception e) {
        }

        assertEquals(TypeConverter.toInt("5000"), Integer.valueOf(5000));
        assertEquals(TypeConverter.toInt("-5000"), Integer.valueOf(-5000));
        assertEquals(TypeConverter.toInt("+5000"), Integer.valueOf(5000));

    }

    public void testToLong() {
        try {
            TypeConverter.toLong("abc");
            fail("Should not be here");
        } catch (Exception e) {
        }

        try {
            TypeConverter.toLong("99559955995599559955");
            fail("Should not be here");
        } catch (Exception e) {

        }

        try {
            TypeConverter.toLong("-99559955995599559955");
            fail("Should not be here");
        } catch (Exception e) {
        }

        try {
            assertEquals(TypeConverter.toLong("5000"), Long.valueOf(5000));
            assertEquals(TypeConverter.toLong("-5000"), Long.valueOf(-5000));
            assertEquals(TypeConverter.toLong("+5000"), Long.valueOf(5000));
        } catch (Exception e) {
            fail("Should not be here");
        }
    }

    public void testToByte() {
        try {
            TypeConverter.toByte("abc");
            fail("Should not be here");
        } catch (Exception e) {
        }

        try {
            TypeConverter.toByte("257");
            fail("Should not be here");
        } catch (Exception e) {
        }

        try {
            TypeConverter.toByte("-257");
            fail("Should not be here");
        } catch (Exception e) {
        }

        assertEquals(TypeConverter.toByte("127"), Byte.valueOf((byte) 127));
        assertEquals(TypeConverter.toByte("-127"), Byte.valueOf((byte) -127));
        assertEquals(TypeConverter.toByte("+127"), Byte.valueOf((byte) 127));
    }

    public void testToFloat() {
        try {
            TypeConverter.toFloat("abc");
            fail("Should not be here");
        } catch (Exception e) {
        }

        assertEquals(TypeConverter.toFloat("127.0"), Float.valueOf(127.0f));
        assertEquals(TypeConverter.toFloat("-127.3"), Float.valueOf(-127.3f));
        assertEquals(TypeConverter.toFloat("+127.5"), Float.valueOf(127.5f));

    }

    public static void testToDouble() {
        try {
            TypeConverter.toDouble("abc");
            fail("Should not be here");
        } catch (Exception e) {
        }

        assertEquals(TypeConverter.toDouble("127.0"), Double.valueOf(127.0));
        assertEquals(TypeConverter.toDouble("-127.3"), Double.valueOf(-127.3));
        assertEquals(TypeConverter.toDouble("+127.5"), Double.valueOf(127.5));
    }

    public static void testToChar() {
        try {
            TypeConverter.toChar("abc");
            fail("Should not be here");
        } catch (Exception e) {

        }

        assertEquals(TypeConverter.toChar("1"), Character.valueOf('1'));
        assertEquals(TypeConverter.toChar("-"), Character.valueOf('-'));

    }

    public static void testToBoolean() {

        assertTrue(TypeConverter.toBoolean(""));
        assertFalse(TypeConverter.toBoolean("no"));
        assertFalse(TypeConverter.toBoolean("false"));
        assertTrue(TypeConverter.toBoolean("true"));
        assertTrue(TypeConverter.toBoolean("yes"));

        try {
            TypeConverter.toBoolean("fail");
            fail("Should not be here");
        } catch (Exception e) {
        }

    }
}
