package java.lang;

import zeechung.minijava.Native;

public final class String {

    private char value[];

    private int hash; 

    public String(char value[]) {
        this.value = Native.copyArray(value, 0, value.length);
    }
 
    public String(char value[], int offset, int count) {
        if (offset < 0) {
            
        }
        if (count <= 0) {
            if (count < 0) {
                 
            }
            if (offset <= value.length) {
                this.value = "".value;
                return;
            }
        }
         
        if (offset > value.length - count) {
             
        }
        this.value = Native.copyArray(value, offset, offset+count);
    }
    
    public String(byte ascii[], int hibyte, int offset, int count) {
        checkBounds(ascii, offset, count);
        char value[] = new char[count];

        if (hibyte == 0) {
            for (int i = count; i-- > 0;) {
                value[i] = (char)(ascii[i + offset] & 0xff);
            }
        } else {
            hibyte <<= 8;
            for (int i = count; i-- > 0;) {
                value[i] = (char)(hibyte | (ascii[i + offset] & 0xff));
            }
        }
        this.value = value;
    }

    
    public String(byte ascii[], int hibyte) {
        this(ascii, hibyte, 0, ascii.length);
    }
 
    private static void checkBounds(byte[] bytes, int offset, int length) {
         
    }
 
    public String(StringBuilder builder) {
        this.value = Native.copyArray(builder.getValue(), 0, builder.length());
    }
 
    String(char[] value, boolean share) {
        this.value = value;
    }
 
    public int length() {
        return value.length;
    }
 
    public boolean isEmpty() {
        return value.length == 0;
    }
 
    public char charAt(int index) {
        if ((index < 0) || (index >= value.length)) {
       
        }
        return value[index];
    }
 
    void getChars(char dst[], int dstBegin) {
        System.arraycopy(value, 0, dst, dstBegin, value.length);
    }
 
    public void getChars(int srcBegin, int srcEnd, char dst[], int dstBegin) {
        if (srcBegin < 0) {
             
        }
        if (srcEnd > value.length) {
 
        }
        if (srcBegin > srcEnd) {
             
        }
        System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
    }
 
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String)anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }
 
    public int compareTo(String anotherString) {
        int len1 = value.length;
        int len2 = anotherString.value.length;
        int lim = len1<len2?len1:len2;
        char v1[] = value;
        char v2[] = anotherString.value;

        int k = 0;
        while (k < lim) {
            char c1 = v1[k];
            char c2 = v2[k];
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }
        return len1 - len2;
    }
 
    public boolean regionMatches(int toffset, String other, int ooffset,
            int len) {
        char ta[] = value;
        int to = toffset;
        char pa[] = other.value;
        int po = ooffset;
        if ((ooffset < 0) || (toffset < 0)
                || (toffset > (long)value.length - len)
                || (ooffset > (long)other.value.length - len)) {
            return false;
        }
        while (len-- > 0) {
            if (ta[to++] != pa[po++]) {
                return false;
            }
        }
        return true;
    }
 
    public boolean startsWith(String prefix, int toffset) {
        char ta[] = value;
        int to = toffset;
        char pa[] = prefix.value;
        int po = 0;
        int pc = prefix.value.length;
        // Note: toffset might be near -1>>>1.
        if ((toffset < 0) || (toffset > value.length - pc)) {
            return false;
        }
        while (--pc >= 0) {
            if (ta[to++] != pa[po++]) {
                return false;
            }
        }
        return true;
    }
 
    public boolean startsWith(String prefix) {
        return startsWith(prefix, 0);
    }
 
    public boolean endsWith(String suffix) {
        return startsWith(suffix, value.length - suffix.value.length);
    }
 
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }
 
    public int indexOf(String str) {
        return indexOf(str, 0);
    }
 
    public int indexOf(String str, int fromIndex) {
        return indexOf(value, 0, value.length,
                str.value, 0, str.value.length, fromIndex);
    }
 
    static int indexOf(char[] source, int sourceOffset, int sourceCount,
            String target, int fromIndex) {
        return indexOf(source, sourceOffset, sourceCount,
                       target.value, 0, target.value.length,
                       fromIndex);
    }
 
    static int indexOf(char[] source, int sourceOffset, int sourceCount,
            char[] target, int targetOffset, int targetCount,
            int fromIndex) {
        if (fromIndex >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }

        char first = target[targetOffset];
        int max = sourceOffset + (sourceCount - targetCount);

        for (int i = sourceOffset + fromIndex; i <= max; i++) {
 
            if (source[i] != first) {
                while (++i <= max && source[i] != first);
            }

            
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source[j]
                        == target[k]; j++, k++);

                if (j == end) {
                     
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }
 
    public int lastIndexOf(String str) {
        return lastIndexOf(str, value.length);
    }
 
    public int lastIndexOf(String str, int fromIndex) {
        return lastIndexOf(value, 0, value.length,
                str.value, 0, str.value.length, fromIndex);
    }
 
    static int lastIndexOf(char[] source, int sourceOffset, int sourceCount,
            String target, int fromIndex) {
        return lastIndexOf(source, sourceOffset, sourceCount,
                       target.value, 0, target.value.length,
                       fromIndex);
    }
 
    static int lastIndexOf(char[] source, int sourceOffset, int sourceCount,
            char[] target, int targetOffset, int targetCount,
            int fromIndex) {
        
        int rightIndex = sourceCount - targetCount;
        if (fromIndex < 0) {
            return -1;
        }
        if (fromIndex > rightIndex) {
            fromIndex = rightIndex;
        }
         
        if (targetCount == 0) {
            return fromIndex;
        }

        int strLastIndex = targetOffset + targetCount - 1;
        char strLastChar = target[strLastIndex];
        int min = sourceOffset + targetCount - 1;
        int i = min + fromIndex;

    startSearchForLastChar:
        while (true) {
            while (i >= min && source[i] != strLastChar) {
                i--;
            }
            if (i < min) {
                return -1;
            }
            int j = i - 1;
            int start = j - (targetCount - 1);
            int k = strLastIndex - 1;

            while (j > start) {
                if (source[j--] != target[k--]) {
                    i--;
                    continue startSearchForLastChar;
                }
            }
            return start - sourceOffset + 1;
        }
    }
 
    public String concat(String str) {
        if (str.isEmpty()) {
            return this;
        }
        int len = value.length;
        int otherLen = str.length();
        char buf[] = Native.copyArray(value, len + otherLen);
        str.getChars(buf, len);
        return new String(buf, true);
    }
 
    public String replace(char oldChar, char newChar) {
        if (oldChar != newChar) {
            int len = value.length;
            int i = -1;
            char[] val = value;  

            while (++i < len) {
                if (val[i] == oldChar) {
                    break;
                }
            }
            if (i < len) {
                char buf[] = new char[len];
                for (int j = 0; j < i; j++) {
                    buf[j] = val[j];
                }
                while (i < len) {
                    char c = val[i];
                    buf[i] = (c == oldChar) ? newChar : c;
                    i++;
                }
                return new String(buf, true);
            }
        }
        return this;
    }
 
    public String toString() {
        return this;
    }
 
    public char[] toCharArray() {
        // Cannot use Arrays.copyOf because of class initialization order issues
        char result[] = new char[value.length];
        System.arraycopy(value, 0, result, 0, value.length);
        return result;
    }
 
    public static String valueOf(Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }
 
    public static String valueOf(char data[]) {
        return new String(data);
    }
 
    public static String valueOf(char data[], int offset, int count) {
        return new String(data, offset, count);
    }
 
    public static String copyValueOf(char data[], int offset, int count) {
        return new String(data, offset, count);
    }
 
    public static String copyValueOf(char data[]) {
        return new String(data);
    }
 
    public static String valueOf(boolean b) {
        return b ? "true" : "false";
    }
 
    public static String valueOf(char c) {
        char data[] = {c};
        return new String(data, true);
    }
 
    public static String valueOf(int i) {
        return Native.toString(i);
    }
 
    public static String valueOf(long l) {
        return Native.toString(l);
    }
 
    public static String valueOf(float f) {
        return Native.toString(f);
    }
 
    public static String valueOf(double d) {
        return Native.toString(d);
    }
 
}
