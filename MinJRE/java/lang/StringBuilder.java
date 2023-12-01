package java.lang;
 
public final class StringBuilder
    extends AbstractStringBuilder
{
 
    public StringBuilder() {
        super(16);
    }
 
    public StringBuilder(int capacity) {
        super(capacity);
    }
 
    public StringBuilder(String str) {
        super(str.length() + 16);
        append(str);
    }
   
    public StringBuilder append(Object obj) {
        return append(String.valueOf(obj));
    }
 
    public StringBuilder append(String str) {
        super.append(str);
        return this;
    }
 
    public StringBuilder append(char[] str) {
        super.append(str);
        return this;
    }
 
    public StringBuilder append(char[] str, int offset, int len) {
        super.append(str, offset, len);
        return this;
    }
 
    public StringBuilder append(boolean b) {
        super.append(b);
        return this;
    }
 
    public StringBuilder append(char c) {
        super.append(c);
        return this;
    }
 
    public StringBuilder append(int i) {
        super.append(i);
        return this;
    }
 
    public StringBuilder append(long lng) {
        super.append(lng);
        return this;
    }
 
    public StringBuilder append(float f) {
        super.append(f);
        return this;
    }
 
    public StringBuilder append(double d) {
        super.append(d);
        return this;
    }
 
    public StringBuilder delete(int start, int end) {
        super.delete(start, end);
        return this;
    }
 
    public StringBuilder deleteCharAt(int index) {
        super.deleteCharAt(index);
        return this;
    }
 
    public StringBuilder replace(int start, int end, String str) {
        super.replace(start, end, str);
        return this;
    }
 
    public StringBuilder insert(int index, char[] str, int offset,
                                int len)
    {
        super.insert(index, str, offset, len);
        return this;
    }
 
    public StringBuilder insert(int offset, Object obj) {
            super.insert(offset, obj);
            return this;
    }
 
    public StringBuilder insert(int offset, String str) {
        super.insert(offset, str);
        return this;
    }
 
    public StringBuilder insert(int offset, char[] str) {
        super.insert(offset, str);
        return this;
    }
 
    public StringBuilder insert(int offset, boolean b) {
        super.insert(offset, b);
        return this;
    }
 
    public StringBuilder insert(int offset, char c) {
        super.insert(offset, c);
        return this;
    }
 
    public StringBuilder insert(int offset, int i) {
        super.insert(offset, i);
        return this;
    }
 
    public StringBuilder insert(int offset, long l) {
        super.insert(offset, l);
        return this;
    }
 
    public StringBuilder insert(int offset, float f) {
        super.insert(offset, f);
        return this;
    }
 
    public StringBuilder insert(int offset, double d) {
        super.insert(offset, d);
        return this;
    }
 
    public int indexOf(String str) {
        return super.indexOf(str);
    }
 
    public int indexOf(String str, int fromIndex) {
        return super.indexOf(str, fromIndex);
    }
 
    public int lastIndexOf(String str) {
        return super.lastIndexOf(str);
    }
 
    public int lastIndexOf(String str, int fromIndex) {
        return super.lastIndexOf(str, fromIndex);
    }
 
    public String toString() { 
        return new String(value, 0, count);
    }
 
}
