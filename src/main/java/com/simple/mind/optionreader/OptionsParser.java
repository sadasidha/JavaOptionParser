package com.simple.mind.optionreader;

import java.lang.reflect.Field;

public class OptionsParser {
  private static ParseCommand parsed = null;

  /**
   * @param <T>  Generic Class
   * @param args Array of string to be parsed
   * @param c    : Class Type
   * @return: Returns Object of class
   */
  public static <T> T parse(String[] args, Class<T> c) {
    parsed = new ParseCommand(args);
    return prepareObject(c);
  }

  private static boolean isArrayOrList(Field f) {
    if (f.getType().isArray()) {
      return true;
    }

    if (AcceptableList.listTypes.contains(f.getType().getName())) {
      return true;
    }
    return false;
  }

  /**
   * @param <T> Class that needed to be assigned
   * @param c   Class
   * @return Class Object
   * @throws Exception
   */
  private static <T> T prepareObject(Class<T> c) {
    try {
      T obj = null;
      obj = c.getDeclaredConstructor().newInstance();
      Field[] df = c.getDeclaredFields();
      for (Field f : df) {
        // Adding this to avoid $jacocoData error for code coverage test
        if (f.getName().compareTo("$jacocoData") == 0 || AnnotationProcessor.isIgnorable(f))
          continue;
        if (isArrayOrList(f))
          setListAndArray(obj, f);
        else
          handleNonArray(obj, f);
      }
      return obj;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static <T> void setPrimitive(T obj, Field f, String v) {
    try {
      @SuppressWarnings("deprecation")
      boolean b = f.isAccessible();
      f.setAccessible(true);
      switch (f.getType().getName()) {
      case "int":
        f.setInt(obj, TypeConverter.toInt(v));
        break;
      case "long":
        f.setLong(obj, TypeConverter.toLong(v));
        break;
      case "java.lang.String":
        f.set(obj, v);
        break;
      case "java.math.BigInteger":
        f.set(obj, TypeConverter.toBigInt(v));
        break;
      case "char":
        f.set(obj, TypeConverter.toChar(v));
        break;
      case "byte":
        f.set(obj, TypeConverter.toByte(v));
        break;
      case "double":
        f.set(obj, TypeConverter.toDouble(v));
        break;
      case "float":
        f.set(obj, TypeConverter.toFloat(v));
        break;
      case "boolean":
        f.set(obj, TypeConverter.toBoolean(v));
        break;
      default:
        throw new RuntimeException("Very unusual type " + f.getType().getCanonicalName());
      }
      f.setAccessible(b);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static <T> void handleNonArray(T obj, Field f) {
    try {
      @SuppressWarnings("deprecation")
      boolean b = f.isAccessible();
      f.setAccessible(true);
      String key = AnnotationProcessor.getOptionNames(f);
      String defaultValue = AnnotationProcessor.getDefaultSingleValue(f);
      if (f.getType().isPrimitive()) {
        setPrimitive(obj, f, parsed.get(key, defaultValue));
        return;
      }

      switch (f.getType().getName()) {
      case "java.lang.Integer":
      case "Integer":
        f.set(obj, parsed.getInt(key, defaultValue));
        break;
      case "java.lang.Long":
      case "Long":
        f.set(obj, parsed.getLong(key, defaultValue));
        break;
      case "java.lang.String":
      case "String":
        f.set(obj, parsed.get(key, defaultValue));
        break;
      case "java.lang.Character":
      case "Character":
        f.set(obj, parsed.getChar(key, defaultValue));
        break;
      case "java.lang.Byte":
      case "Byte":
        f.set(obj, parsed.getByte(key, defaultValue));
        break;
      case "java.lang.Double":
      case "Double":
        f.set(obj, parsed.getDouble(key, defaultValue));
        break;
      case "java.lang.Float":
      case "Float":
        f.set(obj, parsed.getFloat(key, defaultValue));
        break;
      case "java.lang.Boolean":
      case "Boolean":
        f.set(obj, parsed.getBool(key, defaultValue));
        break;
      case "java.math.BigInteger":
      case "BigInteger":
        f.set(obj, parsed.getBigInt(key, defaultValue));
        break;
      default:
        throw new RuntimeException("We cannot process " + f.getType().getName() + " ");
      }
      f.setAccessible(b);
    } catch (

    Exception e) {
      throw new RuntimeException(e);
    }

  }

  private static int[] objectToIntArr(Integer[] a) {
    int[] ar = new int[a.length];
    int i = 0;
    for (int in : a) {
      ar[i] = in;
      i++;
    }
    return ar;
  }

  private static long[] objectToLongArr(Long[] a) {
    long[] ar = new long[a.length];
    int i = 0;
    for (long in : a) {
      ar[i] = in;
      i++;
    }
    return ar;
  }

  private static byte[] objectToByteArr(Byte[] a) {
    byte[] ar = new byte[a.length];
    int i = 0;
    for (byte in : a) {
      ar[i] = in;
      i++;
    }
    return ar;
  }

  private static char[] objectToCharArr(Character[] a) {
    char[] ar = new char[a.length];
    int i = 0;
    for (char in : a) {
      ar[i] = in;
      i++;
    }
    return ar;
  }

  private static double[] objectToDoubleArr(Double[] a) {
    double[] ar = new double[a.length];
    int i = 0;
    for (double in : a) {
      ar[i] = in;
      i++;
    }
    return ar;
  }

  private static float[] objectToFloatArr(Float[] a) {
    float[] ar = new float[a.length];
    int i = 0;
    for (float in : a) {
      ar[i] = in;
      i++;
    }
    return ar;
  }

  private static boolean[] objectToBoolArr(Boolean[] a) {
    boolean[] ar = new boolean[a.length];
    int i = 0;
    for (boolean in : a) {
      ar[i] = in;
      i++;
    }
    return ar;
  }

  // TODO
  private static <T> void setListAndArray(T obj, Field f) {
    try {
      String memberType;
      if (f.getType().isArray()) {
        memberType = f.getType().getComponentType().getName();
      } else {
        memberType = f.getType().getName();
      }

      String key = AnnotationProcessor.getOptionNames(f);
      @SuppressWarnings("deprecation")
      boolean b = f.isAccessible();
      f.setAccessible(true);
      if (AcceptableList.primitives.contains(memberType)) {
        switch (memberType) {
        case "int":
          f.set(obj, objectToIntArr(parsed.getIntArray(key)));
          break;
        case "java.lang.Integer":
        case "Integer":
          f.set(obj, parsed.getIntArray(key));
          break;
        case "long":
          f.set(obj, objectToLongArr(parsed.getLongArray(key)));
          break;
        case "java.lang.Long":
        case "Long":
          f.set(obj, parsed.getLongArray(key));
          break;
        case "byte":
          f.set(obj, objectToByteArr(parsed.getByteArray(key)));
          break;
        case "java.lang.Byte":
        case "Byte":
          f.set(obj, parsed.getByteArray(key));
          break;
        case "char":
          f.set(obj, objectToCharArr(parsed.getCharArray(key)));
          break;
        case "java.lang.Character":
        case "Character":
          f.set(obj, parsed.getCharArray(key));
          break;
        case "float":
          f.set(obj, objectToFloatArr(parsed.getFloatArray(key)));
          break;
        case "java.lang.Float":
        case "Float":
          f.set(obj, parsed.getFloatArray(key));
          break;
        case "double":
          f.set(obj, objectToDoubleArr(parsed.getDoubleArray(key)));
          break;
        case "java.lang.Double":
        case "Double":
          f.set(obj, parsed.getDoubleArray(key));
          break;
        case "boolean":
          f.set(obj, objectToBoolArr(parsed.getBoolArray(key)));
          break;
        case "java.lang.Boolean":
        case "Boolean":
          f.set(obj, parsed.getBoolArray(key));
          break;
        case "java.lang.String":
        case "String":
          f.set(obj, parsed.getArray(key));
          break;
        case "java.math.BigInteger":
        case "BigInteger":
          f.set(obj, parsed.getBigIntArray(key));
          break;
        default:
          throw new RuntimeException("Type defined in class is not valid: " + memberType);
        }
      } else if (AcceptableList.listTypes.contains(memberType)) {
        String type = AnnotationProcessor.isValidListGenericType(f);
        switch (type) {
        case "java.lang.Integer":
          f.set(obj, parsed.getIntList(key));
          break;
        case "java.lang.Long":
          f.set(obj, parsed.getLongList(key));
          break;
        case "java.lang.Byte":
          f.set(obj, parsed.getByteList(key));
          break;
        case "java.lang.Character":
          f.set(obj, parsed.getCharList(key));
          break;
        case "java.lang.Float":
          f.set(obj, parsed.getFloatList(key));
          break;
        case "java.lang.Double":
          f.set(obj, parsed.getDoubleList(key));
          break;
        case "java.lang.String":
          f.set(obj, parsed.getList(key));
          break;

        case "java.lang.Boolean":
          f.set(obj, parsed.getBoolList(key));
          break;
        case "java.math.BigInteger":
          f.set(obj, parsed.getBigIntList(key));
          break;
        default:
          throw new RuntimeException("Type defined in class is not valid: " + type + " for java.util.List");
        }
      } else {
        throw new RuntimeException("Type defined in class is not valid: " + memberType + " for java.util.List");
      }
      f.setAccessible(b);

    } catch (

    Exception e) {
      throw new RuntimeException(e);
    }
  }
}
