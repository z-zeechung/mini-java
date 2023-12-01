package java.lang;

public class Object /*extends zeechung.minijava.Object*/ {

	public int hashCode() {
		return (int) zeechung.minijava.Native.getPtr(this);
	}
	
    public boolean equals(Object obj) {
        return (zeechung.minijava.Native.getPtr(this) == zeechung.minijava.Native.getPtr(obj));
    }
    
    public java.lang.String toString() {
        return "";
    }
}
