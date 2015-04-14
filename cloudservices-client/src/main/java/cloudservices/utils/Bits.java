package cloudservices.utils;

public class Bits {
	
	private static byte char0(char c) {    return (byte) (c >> 0 & 0xff); }
    private static byte char1(char c) {    return (byte) (c >> 8 & 0xff); }
    public static byte[] charToBytes(char c, boolean bigEndian) {
        byte[] b = new byte[2];
        putChar(b, 0, c, bigEndian);
        return b;
    }
    public static void putChar(byte[] b, int offset, char c, boolean bigEndian) {
        if (bigEndian) 
            putCharB(b, offset, c);
        else 
            putCharL(b, offset, c);
    }
    private static void putCharB(byte[] b, int offset, char c) {
        b[offset + 0] = char1(c);
        b[offset + 1] = char0(c);
    }
    private static void putCharL(byte[] b, int offset, char c) {
        b[offset + 0] = char0(c);
        b[offset + 1] = char1(c);
    }
    
    /**
     * 
     * @param b1 ��λ
     * @param b0 ��λ
     * @return
     */
    private static char markChar(byte b1, byte b0) {
        return (char) ((b1 << 8) | (b0 & 0xff));
    }
    public static char getChar(byte[] b, int offset, boolean bigEndian) {
        if (bigEndian)
            return getCharB(b, offset);
        else 
            return getCharL(b, offset);
    }
    private static char getCharB(byte[] b, int offset) {
        return markChar(b[offset + 0], b[offset + 1]);
    }
    private static char getCharL(byte[] b, int offset) {
        return markChar(b[offset + 1], b[offset + 0]);
    }
    
    
    private static byte short0(short s) { return (byte) (s >> 0 & 0xff); }
    private static byte short1(short s) { return (byte) (s >> 8 & 0xff); }
    public static byte[] shortToBytes(short s, boolean bigEndian) {
        byte[] b = new byte[2];
        putShort(b, 0, s, bigEndian);
        return b;
    }
    public static void putShort(byte[] b, int offset, short s, boolean bigEndian) {
        if (bigEndian) 
            putShortB(b, offset, s);
        else 
            putShortL(b, offset, s);
    }
    private static void putShortB(byte[] b, int offset, short s) {
        b[offset + 0] = short1(s);
        b[offset + 1] = short0(s);
    }
    private static void putShortL(byte[] b, int offset, short s) {
        b[offset + 0] = short0(s);
        b[offset + 1] = short1(s);
    }
    
    private static short markShort(byte b1, byte b0) {
        return (short) ((b1 << 8) | (b0 & 0xff));
    }
    public static short getShort(byte[] b, int offset, boolean bigEndian) {
        if (bigEndian)
            return getShortB(b, offset);
        else
            return getShortL(b, offset);
    }
    private static short getShortB(byte[] b, int offset) {
        return markShort(b[offset + 0], b[offset + 1]);
    }
    private static short getShortL(byte[] b, int offset) {
        return markShort(b[offset + 1], b[offset + 0]);
    }
    
    
    private static byte int0(int i) { return (byte) (i >> 0 & 0xff); }
    private static byte int1(int i) { return (byte) (i >> 8 & 0xff); }
    private static byte int2(int i) { return (byte) (i >> 16 & 0xff); }
    private static byte int3(int i) { return (byte) (i >> 24 & 0xff); }
    public static byte[] intToBytes(int i, boolean bigEndian) {
        byte[] b = new byte[4];
        putInt(b, 0, i, bigEndian);
        return b;
    } 
    public static void putInt(byte[] b, int offset, int i, boolean bigEndian) {
        if (bigEndian) 
            putIntB(b, offset, i);
        else 
            putIntL(b, offset, i);
    }
    private static void putIntB(byte[] b, int offset, int i) {
        b[offset + 0] = int3(i);
        b[offset + 1] = int2(i);
        b[offset + 2] = int1(i);
        b[offset + 3] = int0(i);
    }
    private static void putIntL(byte[] b, int offset, int i) {
        b[offset + 0] = int0(i);
        b[offset + 1] = int1(i);
        b[offset + 2] = int2(i);
        b[offset + 3] = int3(i);
    }
    
    private static int markInt(byte b3, byte b2, byte b1, byte b0) {
        return (((int)(b3 & 0xff)) << 24) | 
               (((int)(b2 & 0xff)) << 16) | 
               (((int)(b1 & 0xff)) << 8) | 
               (((int)(b0 & 0xff)) << 0);
    }
    public static int getInt(byte[] b, int offset, boolean bigEndian) {
        if (bigEndian)
            return getIntB(b, offset);
        else 
            return getIntL(b, offset);
    }
    private static int getIntB(byte[] b, int offset) {
        return markInt(b[offset + 0], b[offset + 1], b[offset + 2], b[offset + 3]);
    }
    private static int getIntL(byte[] b, int offset) {
        return markInt(b[offset + 3], b[offset + 2], b[offset + 1], b[offset + 0]);
    }
    
    
    private static byte long0(long l) { return (byte) (l >> 0 & 0xff); }
    private static byte long1(long l) { return (byte) (l >> 8 & 0xff); }
    private static byte long2(long l) { return (byte) (l >> 16 & 0xff); }
    private static byte long3(long l) { return (byte) (l >> 24 & 0xff); }
    private static byte long4(long l) { return (byte) (l >> 32 & 0xff); }
    private static byte long5(long l) { return (byte) (l >> 40 & 0xff); }
    private static byte long6(long l) { return (byte) (l >> 48 & 0xff); }
    private static byte long7(long l) { return (byte) (l >> 56 & 0xff); }
    public static byte[] longToBytes(long l, boolean bigEndian) {
        byte[] b = new byte[8];
        putLong(b, 0, l, bigEndian);
        return b;
    }
    public static void putLong(byte[] b, int offset, long l, boolean bigEndian) {
        if (bigEndian) 
            putLongB(b, offset, l);
        else 
            putLongL(b, offset, l);
    }
    private static void putLongB(byte[] b, int offset, long l) {
        b[offset + 0] = long7(l);
        b[offset + 1] = long6(l);
        b[offset + 2] = long5(l);
        b[offset + 3] = long4(l);
        b[offset + 4] = long3(l);
        b[offset + 5] = long2(l);
        b[offset + 6] = long1(l);
        b[offset + 7] = long0(l);
    }
    private static void putLongL(byte[] b, int offset, long l) {
        b[offset + 0] = long0(l);
        b[offset + 1] = long1(l);
        b[offset + 2] = long2(l);
        b[offset + 3] = long3(l);
        b[offset + 4] = long4(l);
        b[offset + 5] = long5(l);
        b[offset + 6] = long6(l);
        b[offset + 7] = long7(l);
    }
    
    private static long markLong(byte b7, byte b6, byte b5, byte b4,
            byte b3, byte b2, byte b1, byte b0) {
        return (((long)(b7 & 0xff)) << 56) |
               (((long)(b6 & 0xff)) << 48) |
               (((long)(b5 & 0xff)) << 40) |
               (((long)(b4 & 0xff)) << 32) |
               (((long)(b3 & 0xff)) << 24) | 
               (((long)(b2 & 0xff)) << 16) | 
               (((long)(b1 & 0xff)) << 8) | 
               (((long)(b0 & 0xff)) << 0);
    }
    public static long getLong(byte[] b, int offset, boolean bigEndian) {
        if (bigEndian) 
            return getLongB(b, offset);
        else
            return getLongL(b, offset);
    }
    private static long getLongB(byte[] b, int offset) {
        return markLong(b[offset + 0], b[offset + 1], b[offset + 2], b[offset + 3], 
                        b[offset + 4], b[offset + 5], b[offset + 6], b[offset + 7]);
    }
    private static long getLongL(byte[] b, int offset) {
        return markLong(b[offset + 7], b[offset + 6], b[offset + 5], b[offset + 4], 
                        b[offset + 3], b[offset + 2], b[offset + 1], b[offset + 0]);
    }
    
    
    public static byte[] floatToBytes(float f, boolean bigEndian) {
        byte[] b = new byte[4];
        putFloat(b, 0, f, bigEndian);
        return b;
    }
    public static void putFloat(byte[] b, int offset, float f, boolean bigEndian) {
        if (bigEndian) 
            putFloatB(b, offset, f);
        else 
            putFloatL(b, offset, f);
    }
    private static void putFloatB(byte[] b, int offset, float f) {
        putIntB(b, offset, Float.floatToRawIntBits(f));
    }
    private static void putFloatL(byte[] b, int offset, float f) {
        putIntL(b, offset, Float.floatToRawIntBits(f));
    }
    
    private static float markFloat(byte b3, byte b2, byte b1, byte b0) {
        int fi = markInt(b3, b2, b1, b0);
        return Float.intBitsToFloat(fi);
    } 
    public static float getFloat(byte[] b, int offset, boolean bigEndian) {
        if (bigEndian)
            return getFloatB(b, offset);
        else 
            return getFloatL(b, offset);
    }
    private static float getFloatB(byte[] b, int offset) {
        return markFloat(b[offset + 0], b[offset + 1], b[offset + 2], b[offset + 3]);
    }
    private static float getFloatL(byte[] b, int offset) {
        return markFloat(b[offset + 3], b[offset + 2], b[offset + 1], b[offset + 0]);
    }
    
    
    
    public static byte[] doubleToBytes(double d, boolean bigEndian) {
        byte[] b = new byte[8];
        putDouble(b, 0, d, bigEndian);
        return b;
    }
    public static void putDouble(byte[] b, int offset, double d, boolean bigEndian) {
        if (bigEndian) 
            putDoubleB(b, offset, d);
        else
            putDoubleL(b, offset, d);
    }
    private static void putDoubleB(byte[] b, int offset, double d) {
        putLongB(b, offset, Double.doubleToRawLongBits(d));
    }
    private static void putDoubleL(byte[] b, int offset, double d) {
        putLongL(b, offset, Double.doubleToRawLongBits(d));
    }
    
    private static double markDouble(byte b7, byte b6, byte b5, byte b4,
            byte b3, byte b2, byte b1, byte b0) {
        long dl = markLong(b7, b6, b5, b4, b3, b2, b1, b0);
        return Double.longBitsToDouble(dl);
    }
    public static double getDouble(byte[] b, int offset, boolean bigEndian) {
        if (bigEndian) 
            return getDoubleB(b, offset);
        else 
            return getDoubleL(b, offset);
    }
    private static double getDoubleB(byte[] b, int offset) {
        return markDouble(b[offset + 0], b[offset + 1], b[offset + 2], b[offset + 3], 
                          b[offset + 4], b[offset + 5], b[offset + 6], b[offset + 7]);
    }
    private static double getDoubleL(byte[] b, int offset) {
        return markDouble(b[offset + 7], b[offset + 6], b[offset + 5], b[offset + 4], 
                b[offset + 3], b[offset + 2], b[offset + 1], b[offset + 0]);
    }
}
