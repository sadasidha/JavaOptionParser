package com.simple.mind.optionreader;

import static com.simple.mind.optionreader.StringFormatter.formatName;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class ParseCommand {
    private HashMap<String, List<String>> map = new HashMap<String, List<String>>();

    private static void checkAndthrow(String v) {
        final String m = "^[a-zA-Z0-9-_]+[\\[]{0,1}$";
        if (!v.matches(m)) {
            throw new RuntimeException("Invalid parameter: " + v + ". allowd: " + m);
        }
    }

    public ParseCommand(String[] args) {
        List<String> current = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].matches("^--.+$")) {
                checkAndthrow(args[i]);
                String optionName = formatName(args[i]);
                current = map.get(optionName);
                if (current == null) {
                    current = new ArrayList<String>();
                    map.put(optionName, current);
                }
                if (i + 1 == args.length || args[i + 1].matches("^--.+$"))
                    current.add("true");
            } else {
                current.add(args[i]);
            }
        }
    }

    public int getOptionCount() {
        return map.size();
    }

    private String check(String key) {
        List<String> l = map.get(key);
        if (l == null)
            return null;
        if (l.size() > 1)
            throw new RuntimeException("Not Single Value");
        return l.get(0);
    }

    public String get(String key) {
        return check(key);
    }

    public List<String> getList(String key) {
        return map.get(key);
    }

    public String[] getArray(String key) {
        return map.get(key).toArray(new String[map.get(key).size()]);
    }

    public Integer getInt(String key) {
        return TypeConverter.toInt(check(key));
    }

    public List<Integer> getIntList(String key) {
        List<Integer> l = new ArrayList<Integer>();
        for (String s : map.get(key)) {
            l.add(TypeConverter.toInt(s));
        }
        return l;
    }

    public Integer[] getIntArray(String key) {
        List<String> l = map.get(key);
        Integer[] a = new Integer[l.size()];
        int i = 0;
        for (String s : l) {
            a[i] = TypeConverter.toInt(s);
            i++;
        }
        return a;
    }

    public Long getLong(String key) {
        return TypeConverter.toLong(check(key));
    }

    public List<Long> getLongList(String key) {
        List<Long> l = new ArrayList<Long>();
        for (String s : map.get(key)) {
            l.add(TypeConverter.toLong(s));
        }
        return l;
    }

    public Long[] getLongArray(String key) {
        List<String> l = map.get(key);
        Long[] a = new Long[l.size()];
        int i = 0;
        for (String s : l) {
            a[i] = TypeConverter.toLong(s);
            i++;
        }
        return a;
    }

    public Double getDouble(String key) {
        return TypeConverter.toDouble(check(key));
    }

    public List<Double> getDoubleList(String key) {
        List<Double> l = new ArrayList<Double>();
        for (String s : map.get(key)) {
            l.add(TypeConverter.toDouble(s));
        }
        return l;
    }

    public Double[] getDoubleArray(String key) {
        List<String> l = map.get(key);
        Double[] a = new Double[l.size()];
        int i = 0;
        for (String s : l) {
            a[i] = TypeConverter.toDouble(s);
            i++;
        }
        return a;
    }

    public Float getFloat(String key) {
        return TypeConverter.toFloat(check(key));
    }

    public List<Float> getFloatList(String key) {
        List<Float> l = new ArrayList<Float>();
        for (String s : map.get(key)) {
            l.add(TypeConverter.toFloat(s));
        }
        return l;
    }

    public Float[] getFloatArray(String key) {
        List<String> l = map.get(key);
        Float[] a = new Float[l.size()];
        int i = 0;
        for (String s : l) {
            a[i] = TypeConverter.toFloat(s);
            i++;
        }
        return a;
    }

    public Boolean getBool(String key) {
        return TypeConverter.toBoolean(check(key));
    }

    public List<Boolean> getBoolList(String key) {
        List<Boolean> l = new ArrayList<Boolean>();
        for (String s : map.get(key)) {
            l.add(TypeConverter.toBoolean(s));
        }
        return l;
    }

    public Boolean[] getBoolArray(String key) {
        List<String> l = map.get(key);
        Boolean[] a = new Boolean[l.size()];
        int i = 0;
        for (String s : l) {
            a[i] = TypeConverter.toBoolean(s);
            i++;
        }
        return a;
    }

    public Character getChar(String key) {
        return TypeConverter.toChar(check(key));
    }

    public List<Character> getCharList(String key) {
        List<Character> l = new ArrayList<Character>();
        for (String s : map.get(key)) {
            l.add(TypeConverter.toChar(s));
        }
        return l;
    }

    public Character[] getCharArray(String key) {
        List<String> l = map.get(key);
        Character[] a = new Character[l.size()];
        int i = 0;
        for (String s : l) {
            a[i] = TypeConverter.toChar(s);
            i++;
        }
        return a;
    }

    public Byte getByte(String key) {
        return TypeConverter.toByte(check(key));
    }
    
    public Byte getByte(String key, String []def) {
        return TypeConverter.toByte(check(key));
    }

    public List<Byte> getByteList(String key) {
        List<Byte> l = new ArrayList<Byte>();
        for (String s : map.get(key)) {
            l.add(TypeConverter.toByte(s));
        }
        return l;
    }

    public List<Byte> getByteList(String key, String[] def) {
        List<Byte> l = getByteList(key);
        if (l != null)
            return l;
        l = new ArrayList<Byte>();
        for (String s : def) {
            l.add(TypeConverter.toByte(s));
        }
        return l;
    }

    public Byte[] getByteArray(String key) {
        List<String> l = map.get(key);
        if (l == null)
            return null;
        Byte[] a = new Byte[l.size()];
        int i = 0;
        for (String s : l) {
            a[i] = TypeConverter.toByte(s);
            i++;
        }
        return a;
    }

    public Byte[] getByteArray(String key, String[] def) {
        Byte[] b = getByteArray(key);
        if (b != null)
            return b;
        b = new Byte[def.length];
        int i = 0;
        for (String s : def) {
            b[i] = TypeConverter.toByte(s);
            i++;
        }
        return b;
    }

    public BigInteger getBigInt(String key) {
        return TypeConverter.toBigInt(check(key));
    }

    public BigInteger getBigInt(String key, String def) {
        BigInteger bi = TypeConverter.toBigInt(check(key));
        if (bi != null)
            return bi;
        return TypeConverter.toBigInt(key);
    }

    public List<BigInteger> getBigIntList(String key) {
        List<BigInteger> l = new ArrayList<BigInteger>();
        if (!map.containsKey(key))
            return null;
        for (String s : map.get(key)) {
            l.add(TypeConverter.toBigInt(s));
        }
        return l;
    }

    public List<BigInteger> getBigIntList(String key, String[] def) {
        List<BigInteger> l = getBigIntList(key);
        if (l != null)
            return l;
        l = new ArrayList<BigInteger>();
        for (String s : def) {
            l.add(TypeConverter.toBigInt(s));
        }
        return l;
    }

    public BigInteger[] getBigIntArray(String key) {
        List<String> l = map.get(key);
        if (l == null)
            return null;
        BigInteger[] a = new BigInteger[l.size()];
        int i = 0;
        for (String s : l) {
            a[i] = TypeConverter.toBigInt(s);
            i++;
        }
        return a;
    }

    public BigInteger[] getBigIntArray(String key, String[] def) {
        BigInteger[] ret = getBigIntArray(key);
        if (ret != null)
            return ret;
        ret = new BigInteger[def.length];
        int i = 0;
        for (String s : def) {
            ret[i] = TypeConverter.toBigInt(s);
            i++;
        }
        return ret;
    }

    public boolean hasKey(String key) {
        return map.get(key) == null ? false : true;
    }
}
