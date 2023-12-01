package java.lang;

import zeechung.minijava.Native;

public final class System {
	
    //public final static InputStream in = null;
     
    //public final static PrintStream out = null;
 
    //public final static PrintStream err = null;
	
	public static String input() {
		return Native.input();
	}
	
	public static void print(String str) {
		Native.print(str);
	}
 
    public static long currentTimeMillis() {
    	return Native.currentTimeMillis();
    }
 
    public static long nanoTime() {
    	return Native.nanoTime();
    }
 
    public static void arraycopy(char[] src,  int  srcPos,
    							 char[] dest, int destPos,
                                        int length) {
    	Native.copyArray(src, srcPos, dest, destPos, length);
    }
 
    public static int identityHashCode(Object x) {
    	return (int) Native.getPtr(x);
    }
 
    public static void exit(int status) {
        Native.exit(status);
    }
}