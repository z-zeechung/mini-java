package java.lang;

import zeechung.minijava.Native;
 
abstract class AbstractStringBuilder {
     
    char[] value;
 
    int count; 
 
    AbstractStringBuilder(int capacity) {
        value = new char[capacity];
    }
 
    public int length() {
        return count;
    }
 
    public int capacity() {
        return value.length;
    }
 
    public void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity > 0)
            ensureCapacityInternal(minimumCapacity);
    }
 
    private void ensureCapacityInternal(int minimumCapacity) { 
        if (minimumCapacity - value.length > 0) {
            value = Native.copyArray(value,
                    newCapacity(minimumCapacity));
        }
    }
 
    private static final int MAX_ARRAY_SIZE = Native.maxIntValue() - 8;
 
    private int newCapacity(int minCapacity) { 
        int newCapacity = (value.length << 1) + 2;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        return (newCapacity <= 0 || MAX_ARRAY_SIZE - newCapacity < 0)
            ? hugeCapacity(minCapacity)
            : newCapacity;
    }

    private int hugeCapacity(int minCapacity) {
        if (Native.maxIntValue() - minCapacity < 0) { 
        }
        return (minCapacity > MAX_ARRAY_SIZE)
            ? minCapacity : MAX_ARRAY_SIZE;
    }
 
    public void trimToSize() {
        if (count < value.length) {
            value = Native.copyArray(value, count);
        }
    } 
    
    public char charAt(int index) { 
        return value[index];
    } 
    
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
    { 
        System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
    }
 
    public void setCharAt(int index, char ch) { 
        value[index] = ch;
    }
 
    public AbstractStringBuilder append(Object obj) {
        return append(String.valueOf(obj));
    }
    
    public AbstractStringBuilder append(String str) {
        if (str == null)
            return appendNull();
        int len = str.length();
        ensureCapacityInternal(count + len);
        str.getChars(0, len, value, count);
        count += len;
        return this;
    }
    
    private AbstractStringBuilder appendNull() {
        int c = count;
        ensureCapacityInternal(c + 4);
        final char[] value = this.value;
        value[c++] = 'n';
        value[c++] = 'u';
        value[c++] = 'l';
        value[c++] = 'l';
        count = c;
        return this;
    }
 
    public AbstractStringBuilder append(char[] str) {
        int len = str.length;
        ensureCapacityInternal(count + len);
        System.arraycopy(str, 0, value, count, len);
        count += len;
        return this;
    }
 
    public AbstractStringBuilder append(char str[], int offset, int len) {
        if (len > 0)                // let arraycopy report AIOOBE for len < 0
            ensureCapacityInternal(count + len);
        System.arraycopy(str, offset, value, count, len);
        count += len;
        return this;
    }
 
    public AbstractStringBuilder append(boolean b) {
        if (b) {
            ensureCapacityInternal(count + 4);
            value[count++] = 't';
            value[count++] = 'r';
            value[count++] = 'u';
            value[count++] = 'e';
        } else {
            ensureCapacityInternal(count + 5);
            value[count++] = 'f';
            value[count++] = 'a';
            value[count++] = 'l';
            value[count++] = 's';
            value[count++] = 'e';
        }
        return this;
    }
 
    public AbstractStringBuilder append(char c) {
        ensureCapacityInternal(count + 1);
        value[count++] = c;
        return this;
    }
 
    public AbstractStringBuilder append(int i) {
        append(String.valueOf(i));
        return this;
    }
 
    public AbstractStringBuilder append(long l) {
    	append(String.valueOf(l));
        return this;
    }
 
    public AbstractStringBuilder append(float f) {
    	append(String.valueOf(f));
        return this;
    }
 
    public AbstractStringBuilder append(double d) {
    	append(String.valueOf(d));
        return this;
    } 
    
    public AbstractStringBuilder delete(int start, int end) { 
        int len = end - start;
        if (len > 0) {
            System.arraycopy(value, start+len, value, start, count-end);
            count -= len;
        }
        return this;
    }
 
    public AbstractStringBuilder deleteCharAt(int index) { 
        System.arraycopy(value, index+1, value, index, count-index-1);
        count--;
        return this;
    }
 
    public AbstractStringBuilder replace(int start, int end, String str) { 
        if (end > count)
            end = count;
        int len = str.length();
        int newCount = count + len - (end - start);
        ensureCapacityInternal(newCount);

        System.arraycopy(value, end, value, start + len, count - end);
        str.getChars(value, start);
        count = newCount;
        return this;
    }
 
    public String substring(int start) {
        return substring(start, count);
    }
 
    public String substring(int start, int end) {
    	return new String(value, start, end - start);
    }
 
    public AbstractStringBuilder insert(int index, char[] str, int offset,
                                        int len)
    { 
        ensureCapacityInternal(count + len);
        System.arraycopy(value, index, value, index + len, count - index);
        System.arraycopy(str, offset, value, index, len);
        count += len;
        return this;
    }
 
    public AbstractStringBuilder insert(int offset, Object obj) {
        return insert(offset, String.valueOf(obj));
    }
 
    public AbstractStringBuilder insert(int offset, String str) { 
        if (str == null)
            str = "null";
        int len = str.length();
        ensureCapacityInternal(count + len);
        System.arraycopy(value, offset, value, offset + len, count - offset);
        str.getChars(value, offset);
        count += len;
        return this;
    }
 
    public AbstractStringBuilder insert(int offset, char[] str) { 
        int len = str.length;
        ensureCapacityInternal(count + len);
        System.arraycopy(value, offset, value, offset + len, count - offset);
        System.arraycopy(str, 0, value, offset, len);
        count += len;
        return this;
    }
 
    public AbstractStringBuilder insert(int offset, boolean b) {
        return insert(offset, String.valueOf(b));
    }
 
    public AbstractStringBuilder insert(int offset, char c) {
        ensureCapacityInternal(count + 1);
        System.arraycopy(value, offset, value, offset + 1, count - offset);
        value[offset] = c;
        count += 1;
        return this;
    }
 
    public AbstractStringBuilder insert(int offset, int i) {
        return insert(offset, String.valueOf(i));
    }
 
    public AbstractStringBuilder insert(int offset, long l) {
        return insert(offset, String.valueOf(l));
    }
 
    public AbstractStringBuilder insert(int offset, float f) {
        return insert(offset, String.valueOf(f));
    }
 
    public AbstractStringBuilder insert(int offset, double d) {
        return insert(offset, String.valueOf(d));
    }
 
    public int indexOf(String str) {
        return indexOf(str, 0);
    }
 
    public int indexOf(String str, int fromIndex) {
        return String.indexOf(value, 0, count, str, fromIndex);
    }
 
    public int lastIndexOf(String str) {
        return lastIndexOf(str, count);
    }
 
    public int lastIndexOf(String str, int fromIndex) {
        return String.lastIndexOf(value, 0, count, str, fromIndex);
    }
 
    public abstract String toString();
 
    final char[] getValue() {
        return value;
    }

}
