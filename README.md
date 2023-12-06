# mini-java
Mini Java is a project that aims to convert Java class code into C code, allowing the compilation of cross-platform light-weight Java program.

Currently, this project is not functionable and has much work to do with (and many bugs to be fixed). Any commitment is welcomed.

**TODO**
+ MinJRE (Minimum JRE) implemention
+ auto memory management
+ garbage collector
+ native method invocation
+ testing
+ JavaSE implemention

# Quick Start
1. Download all the files
2. Create a Java file under ```/MinJRE/java/lang/```. For example, ```/MinJRE/java/lang/HelloWorld.java```.
3. Write code in the file. Below is an example code:
```
package java.lang;
import zeechung.minijava.Native;

public class HelloWorld{
  public static void main(String[] args){
    Native.print("Hello World!\n");  //I haven't yet finished java.lang.PrintStream.
                                       This would be replaced by System.out.println() in the future
    int a = 3;
    Native.print(a+"\n");
  }
}
```
4. Compile the whole package into Java class file.
5. Configurate ```path/to/your/.class``` in ```Parser.java```. Run this file, and you will see your C code in ```log.txt```.
6. Compile the C code in ```log.txt``` and run it.
