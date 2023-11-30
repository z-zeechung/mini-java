package mini_java.parser;

import java.util.Random;

public class HelloWorld {
	
	static int result;

	public static void main(String[] args) {		
        int a = 111, b = 222;
        result = add(a, b);
	}
	
	static int add(int a, int b) {
		return a+b;
	}
}
