package zeechung.minijava;

public class Native {

	public static long getPtr(Object obj) {return 0;}
	
	public static char[] copyArray(char[] c, int from, int to) {return null;}
	
	public static char[] copyArray(char[] c, int len) {return null;}
	
	public static void copyArray(char[] src, int srcPos, char[] dest, int destPos, int length) {}
	
	public static void exit(int code) {}
	
	public static long currentTimeMillis() {return 0;}
	
	public static long nanoTime() {return 0;}
	
	public static String input() {return null;}
	
	public static void print(String str) {}
	
	public static String toString(int i) {return null;}
	
	public static String toString(long l) {return null;}
	
	public static String toString(float f) {return null;}
	
	public static String toString(double d) {return null;}
	
	public static int maxIntValue() {return 0;}
	
	public static int minIntValue() {return 0;}
	
	public static int intStringSize(int i) {return 0;}
	
	public static void getIntChars(int i, int index, char[] buf) {}
	
	public static int maxLongValue() {return 0;}
	
	public static int minLongValue() {return 0;}
	
	public static int longStringSize(long l) {return 0;}
	
	public static void getLongChars(long l, int index, char[] buf) {}
	
	public static int maxFloatValue() {return 0;}
	
	public static int minFloatValue() {return 0;}
	
	public static int floatStringSize(float f) {return 0;}
	
	public static void getFloatChars(float f, int index, char[] buf) {}	
	
	public static int maxDoubleValue() {return 0;}
	
	public static int minDoubleValue() {return 0;}
	
	public static int doubleStringSize(double d) {return 0;}
	
	public static void getDoubleChars(double d, int index, char[] buf) {}	
	
}
