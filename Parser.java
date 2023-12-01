package mini_java.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class Parser {
	
	public static void main(String[] args) throws IOException {
		
		System.setOut(new PrintStream("log.txt"));
		
		String path = "D:\\Codes\\Eclipse\\java coding\\min_jre\\bin\\java\\lang\\HelloWorld.class";
		String fileName = new File(path).getName();
		String folder = new File(path).getParent();
		
		new Parser(Files.readAllBytes(Paths.get(path)));
		
		for(String f: new File(folder).list()) {
			if(f.equals(fileName)) continue;
			new Parser(Files.readAllBytes(Paths.get(folder+"/"+f)));
		}
		
		System.out.println(Parser.getCCode());
	}
	
	
	
	private static StringBuffer declarations = new StringBuffer();
	
	private static StringBuffer declarations2 = new StringBuffer();
	
	private static StringBuffer codes = new StringBuffer();
	
	private static StringBuffer main = new StringBuffer();
	
	static{
		declarations.append("#include <stdbool.h>\n");
		declarations.append("#include <stdlib.h>\n");
		declarations.append("#include <stdarg.h>\n");
		declarations.append("#include <string.h>\n");
		declarations.append("#include <wchar.h>\n");
		declarations.append("#include <locale.h>\n");
		declarations.append("#include <time.h>\n");
		main.insert(0, "setlocale(LC_CTYPE, \"\");\n");
		declarations.append("\n");
		
		declarations.append("typedef char jbyte;\n");
		declarations.append("typedef short jshort;\n");
		declarations.append("typedef long int jint;\n");
		declarations.append("typedef long long jlong;\n");
		declarations.append("typedef float jfloat;\n");
		declarations.append("typedef double jdouble;\n");
		declarations.append("typedef wchar_t jchar;\n");
		declarations.append("typedef char jboolean;\n");
		declarations.append("typedef void (*Function)();\n");
		declarations.append("struct jinfo{struct jinfo* parent; Function* funcs; int* index; int count;};\n");
		declarations.append("#define "+toCName("zeechung/minijava/Object")+"_fields struct jinfo* info;\n");
		declarations.append("typedef struct {"+toCName("zeechung/minijava/Object")+"_fields}* "+toCName("zeechung/minijava/Object")+";\n");
		declarations.append("#define zeechung_d_minijava_d_Object_info jnull\n");
		declarations.append("typedef "+toCName("zeechung/minijava/Object")+" jobject;\n");
		declarations.append("typedef union{jint INT; jlong LONG; jfloat FLOAT; jdouble DOUBLE; jbyte BYTE; jshort SHORT; jboolean BOOLEAN; jobject OBJECT; jchar CHAR;} slot;\n");
		declarations.append("#define "+toCName("zeechung/minijava/Array")+"_fields slot* base; int length;\n");
		declarations.append("typedef struct "+toCName("zeechung/minijava/Array")+" {"+toCName("zeechung/minijava/Object")+"_fields "+toCName("zeechung/minijava/Array")+"_fields}* "+toCName("zeechung/minijava/Array")+";\n");
		declarations.append("typedef "+toCName("zeechung/minijava/Array")+" jarray;\n");
		declarations.append("#define jnull NULL\n");
		declarations.append("\n");
		
		declarations.append("#define InitStack(x) int stack_len = x; slot stack[x]; int stack_top = -1;\n");
		declarations.append("#define PushData(data, type) stack_top++; stack[stack_top].type = data;\n");
		declarations.append("#define PushSlot(slot) stack_top++; stack[stack_top] = slot;\n");
		declarations.append("#define PopData(data, type) data = stack[stack_top].type; stack_top--;\n");
		declarations.append("#define PopSlot(slot) slot = stack[stack_top]; stack_top--;\n");
		declarations.append("#define StackTop() (stack[stack_top])\n");
		declarations.append("#define StackOffset(offset) (stack[stack_top-offset])\n");
		declarations.append("#define IncStack(x) stack_top=stack_top+x;\n");
		declarations.append("#define DecStack(x) stack_top=stack_top-x;\n");
		declarations.append("\n");
		
		declarations.append("jarray NewArray(int length){\n");
		declarations.append("	jarray arr = (jarray)malloc(sizeof(struct "+toCName("zeechung/minijava/Array")+"));\n");
		declarations.append("	slot* base = (slot*)malloc(sizeof(slot)*length);\n");
		declarations.append("	arr->base = base;\n");
		declarations.append("	arr->length = length;\n");
		declarations.append("	return arr;\n");
		declarations.append("}\n");
		declarations.append("\n");
		
		declarations.append("Function FetchMethod(struct jinfo* info, int target){\r\n"
						  + "	if(info==jnull) return NULL;\r\n"
						  + "	int* arr = info->index;\r\n"
						  + "	int low = 0;\r\n"
						  + "	int high = info->count - 1;\r\n"
						  + "	while (low <= high) {\r\n"
						  + "        int mid = low + (high - low) / 2;\r\n"
						  + "        if (arr[mid] > target) {\r\n"
						  + "            high = mid - 1;\r\n"
						  + "        }\r\n"
						  + "        else if (arr[mid] < target) {\r\n"
						  + "            low = mid + 1;\r\n"
						  + "        }\r\n"
						  + "        else {\r\n"
						  + "        	return info->funcs[mid];\r\n"
						  + "        }\r\n"
						  + "    }\r\n"
						  + "    return FetchMethod(info->parent, target);\r\n"
						  + "}\r\n"
						  + "#define InvokeMethod(obj, idx, type, ...) (((type)FetchMethod(obj->info, idx))(__VA_ARGS__))\n\n");
		
		try {
			for(String ln:Files.readAllLines(Paths.get("zeechung.minijava.Native.decl"))) {
				declarations.append(ln+"\n");
			}
			
			for(String ln:Files.readAllLines(Paths.get("zeechung.minijava.Native.def"))) {
				codes.append(ln+"\n");
			}
		} catch (IOException e1) {}
		
		codes.append(toCName("java/lang/String")+" ToString(jchar* chars){\r\n"
				  + "	jarray arr = (jarray)malloc(sizeof(struct "+toCName("zeechung/minijava/Array")+"));\r\n"
				  + "	arr->info = jnull;\r\n"
				  + "	int len = wcslen(chars);\r\n"
				  + "	arr->length = len;\n"
				  + "	slot* base = (slot*)malloc(sizeof(slot)*(len+1));\r\n"
				  + "	for(int i=0;i<len;i++){\r\n"
				  + "		base[i].CHAR = chars[i];\r\n"
				  + "	}\r\n"
				  + "	arr->base = base;\r\n"
				  + "	\r\n"
				  + "	"+toCName("java/lang/String")+" string = ("+toCName("java/lang/String")+") malloc(sizeof(struct "+toCName("java/lang/String")+"));\r\n"
				  + "	string->info = "+toCName("java/lang/String")+"_info;\r\n"
				  + "	string->value = arr;\r\n"
				  + "	\r\n"
				  + "	return string;\r\n"
				  + "}\n\n ");
		
		try {
			opCodes = Files.readAllLines(Paths.get("opCodes.txt"), StandardCharsets.US_ASCII);
		} catch (IOException e) {}
	}
	
	private static List<String> opCodes;
	
	public static String getCCode() {
		String re =  declarations.toString()+"\n"
				   + declarations2.toString()+"\n"
				   + codes.toString() + "\n"
				   + "int main(){\n"+main+"}";
		declarations = new StringBuffer();
		declarations2 = new StringBuffer();
		codes = new StringBuffer();
		main = new StringBuffer();
		
		return re;
	}
	
	
	
	private int idx = 0;
	
	private byte[] code;
	
	private List constantPool = new LinkedList();
	
	private int clazz;
	
	private int parent;
	
	private int[] impls;
	
	private List<String> stringIds = new LinkedList<String>();
	
	public Parser(byte[] code) {
		
		this.code = code;
		
		parseMagic();
		
		parseVersionNumber();
		
		parseConstantPool();
		/*for(int i=1;i<=constantPool.size();i++) {
			System.out.println(i+" "+constantPool.get(i));
		}*/
		
		parseAccessFlag();
		
		parseClass();
		
		parseParent();
		
		parseImpls();
		
		parseFields();
		
		parseMethods();
		
		parseMain();
		
		//System.out.println(declarations);
		//System.out.println(codes);
		//System.out.println("int main(){\n"+main+"}");
	}

	private void parseMagic() {	//解析魔数
		idx += 4;	//直接跳过
	}

	private void parseVersionNumber() {	//解析版本号
		idx += 4;	//直接跳过
	}
	
	private void parseConstantPool() {
		
		int count = nextU2();
		count--;
		
		constantPool.add(null);

		for(int i=1;i<=count;i++) {
			int tag = nextU1();
			if(tag==1) {	//utf-8
				int len = nextU2();
				String str = nextString(len);
				constantPool.add(str);
			}else if(tag==3) {	//int
				constantPool.add(nextU4());
			}else if(tag==4) {	//float
				constantPool.add(Float.intBitsToFloat(nextU4()));
			}else if(tag==5) {	//long
				constantPool.add(nextU8());
				constantPool.add(null);
				i++;
			}else if(tag==6) {	//double
				constantPool.add(Double.longBitsToDouble(nextU8()));
				constantPool.add(null);
				i++;
			}else if(tag==7) {	//class info
				constantPool.add(nextU2());
			}else if(tag==8) {	//string info, stored as int[1] to distinguish from int within 
								//the ldc(18, 0x12) and ldc_w(19, 0x13) opcode
				constantPool.add(new int[] {nextU2()});
			}else if(tag==9) {	//field 
				constantPool.add(new int[] {nextU2(), nextU2()});
			}else if(tag==10) {	//method 
				constantPool.add(new int[] {nextU2(), nextU2()});
			}else if(tag==11) {	//interface method 
				constantPool.add(new int[] {nextU2(), nextU2()});
			}else if(tag==12) {	//name and type
				constantPool.add(new int[] {nextU2(), nextU2()});
			}else if(tag==15) {	//method handle 
				constantPool.add(new int[] {nextU2(), nextU2()});
			}else if(tag==16) {	//method type 
				constantPool.add(nextU2());
			}else if(tag==18) {	//invoke dynamic
				constantPool.add(new int[] {nextU2(), nextU2()});
			}else {
				
			}
		}
	}
	
	private void parseAccessFlag() {
		idx += 2;
	}
	
	private void parseClass() {
		clazz = nextU2();
		codes.append("\n\n\n//"+getClass(clazz)+"\n");
	}
	
	private void parseParent() {
		parent = nextU2();
	}
	
	private void parseImpls() {
		Set<Integer> s = new HashSet<Integer>();
		int count = nextU2();
		for(int i=0;i<count;i++) {
			s.add((int)nextU2());
		}
		Integer arr[] = new Integer[s.size()];
		s.toArray(arr);
		for(int i=0;i<arr.length;i++) {
			impls[i] = arr[i];
		}
	}
	
	private void parseFields() {
		
		int isConstantValue = -1;
		try {
			isConstantValue = constantPool.indexOf("ConstantValue");
		}catch(Exception e) {}
		
		StringBuffer members = new StringBuffer();
		
		int count = nextU2();
		for(int i=0;i<count;i++) {
			int access = nextU2();
			String name = getUTF8(nextU2());
			String desc = getUTF8(nextU2());
			int valIdx = -1;
			int attrCount = nextU2();
			for(int j=0;j<attrCount;j++) {
				if(nextU2()==isConstantValue) {
					idx+=4;
					valIdx = nextU2();
				}else {
					int len = nextU4();
					idx += len;
				}
			}
			if(isStatic(access)) {
				String fullName = toCName(getClass(clazz))+"_static_"+name;
				if(desc.length()==1) {
					declarations.append(decodeDesc(desc)+" "+fullName+";\n");
				}else {
					declarations.append("jobject "+fullName+";\n");
				}
				if(valIdx!=-1) {
					if(desc.equals("J")) {
						codes.append("jlong"+fullName+" = "+getLong(valIdx)+"L;\n");
					}else if(desc.equals("F")) {
						codes.append("jfloat"+fullName+" = "+Float.toString(getFloat(valIdx))+"f;\n");
					}else if(desc.equals("D")) {
						codes.append("jdouble"+fullName+" = "+Double.toString(getDouble(valIdx))+";\n");
					}else if(desc.equals("Ljava/lang/String;")) {
						codes.append(fullName+" = ToString(L\""+getString(valIdx)+"\");\n");
					}else {
						codes.append(fullName+" = "+getInt(valIdx)+";\n");
					}
				}
			}else {
				if(desc.length()==1||desc.equals("Ljava/lang/String;")) {
					members.append(decodeDesc(desc)+" "+name+"; ");
				}else {
					members.append("jobject "+name+"; ");
				}
			}
		}
		
		declarations.append("#define "+toCName(getClass(clazz))+"_fields ");
		declarations.append(" "+toCName(getClass(parent))+"_fields ");
		declarations.append(members);
		declarations.append("\n");
		declarations2.append("typedef struct "+toCName(getClass(clazz))+" {"+toCName(getClass(clazz))+"_fields}* "+toCName(getClass(clazz))+";\n\n");
	}
	
	private String methodName;
	private String methodDesc;
	private String method;
	private boolean isStatic;
	private void parseMethods() {
		
		int isCode = -1;
		try {
			isCode = constantPool.indexOf("Code");
		}catch(Exception e) {};
		
		Map<Integer, String> info = new HashMap<Integer, String>();
		
		int count = nextU2();
		for(int i=0;i<count;i++) {
			int modifier = nextU2();
			isStatic = isStatic(modifier);
			boolean isVirtual = isVirtual(modifier);
			methodName = getUTF8(nextU2());
			boolean isStaticBlock = methodName.equals("<clinit>");
			methodDesc = getUTF8(nextU2());
			if(!stringIds.contains(methodName+methodDesc)) stringIds.add(methodName+methodDesc);
			info.put(stringIds.indexOf(methodName+methodDesc), methodName+methodDesc);
			methodName = toCName(getClass(clazz)) + "_" + toCName(methodName);
			method = decodeMethodDesc(methodName, methodDesc, isStatic);
			declarations.append(method+";\n");
			int attrCount = nextU2();
			for(int j=0;j<attrCount;j++) {
				int attrName = nextU2();
				if(attrName==isCode) {
					String code = parseCode();
					codes.append(code);
				}else {
					int len = nextU4();
					idx+=len;
				}
			}
			
			if(isStaticBlock) {
				main.insert(0, methodName+toCName(methodDesc)+"();\n");
			}
		}
		
		info = new TreeMap<Integer, String>(info);
		declarations.append("Function "+toCName(getClass(clazz))+"_funcs[]={");
		for(String i: info.values()) {
			declarations.append("(void(*)()) "+toCName(getClass(clazz))+"_"+toCName(i)+",");
		}
		declarations.setCharAt(declarations.length()-1, ' ');
		declarations.append("};\n");
		
		declarations.append("int "+toCName(getClass(clazz))+"_funcsidx[]={");
		for(int i: info.keySet()) {
			declarations.append(i+",");
		}
		declarations.setCharAt(declarations.length()-1, ' ');
		declarations.append("};\n");
		
		declarations.append("struct jinfo* "+toCName(getClass(clazz))+"_info;\n");
		declarations.append("struct jinfo "+toCName(getClass(clazz))+"_infobase = {jnull, &"+toCName(getClass(clazz))+"_funcs, &"+toCName(getClass(clazz))+"_funcsidx, "+info.size()+"};\n");
		declarations.append("struct jinfo* "+toCName(getClass(clazz))+"_info = &"+toCName(getClass(clazz))+"_infobase;\n");
		main.insert(0, toCName(getClass(clazz))+"_info->parent = "+toCName(getClass(parent))+"_info;\n");
	}
	
	private String parseCode() {
		int len = nextU4();
		int tarIdx = idx + len;
		
		StringBuffer code = new StringBuffer();
		
		code.append(method+"{\n");
		
		int stack = nextU2();
		if(stack>0) code.append("\t\tInitStack("+stack+");\n\t\t");
		int locals = nextU2();
		for(int i=0;i<locals;i++) {
			code.append("slot local"+i+"; ");
		}
		code.append("\n");
		
		code.append("\t\tslot opslot1, opslot2, opslot3, opslot4;\n");
		
		code.append("\t\t");
		char[] varTypes = decodeDescs(methodDesc, isStatic);
		for(int i=0;i<varTypes.length;i++) {
			char c = varTypes[i];
			String unionName = "";
			switch(c) {
			case 'L': unionName = "OBJECT"; break;
			case 'B': unionName = "BYTE"; break;
			case 'C': unionName = "CHAR"; break;
			case 'D': unionName = "DOUBLE"; break;
			case 'F': unionName = "FLOAT"; break;
			case 'I': unionName = "INT"; break;
			case 'J': unionName = "LONG"; break;
			case 'S': unionName = "SHORT"; break;
			case 'Z': unionName = "BOOLEAN"; break;
			}
			if(isStatic) {
				code.append("local"+(i)+"."+unionName+" = arg"+(i+1)+"; ");
			}else {
				if(i==0) {
					code.append("local0.OBJECT = this; ");
				}else {
					code.append("local"+(i)+"."+unionName+" = arg"+i+"; ");
				}
			}
		}
		code.append("\n");
		
		int codeLength = nextU4();
		int initIdx = idx;
		while(idx-initIdx<codeLength) {
			try {
			int op = nextU1();
			String line = "GOTO_"+(idx-initIdx-1)+": \t"+opCodes.get(op)+"\n";
			if(op==16 || 
			   (op>=21 && op<=25) ||
			   (op>=54 && op<=58) ||
			   op==188) {
				line = line.replace("U1", Integer.toString(nextU1()));
			}else if(op==17 ||
					 op==168 ||
					 op==189) {
				line = line.replace("U2", Integer.toString(nextU2()));
			}else if((op>=153&&op<=167) ||
					  op==198 ||
					  op==199 ||
					  op==200) {	//goto
				int index = idx - initIdx - 1;
				int offset = op==200?nextU4():nextU2();
				index += op==200?offset:(short)offset;
				line = line.replace("U2", Integer.toString(index)).replace("U4", Integer.toString(index));
			}else if(op>=18&&op<=20) {	//ldc
				String type = "";
				String value = "";
				if(op!=20) {
					int index = op==18?nextU1():nextU2();
					Object o = constantPool.get(index);
					if(o instanceof Integer) {
						type = "INT";
						value = o.toString() + "f";
					}else if(o instanceof Float) {
						type = "FLOAT";
						value = o.toString();
					}else {
						type = "OBJECT";
						String str = (String) constantPool.get(((int[])o)[0]);
						value = str==null?"jnull":"ToString(L\""+str+"\")";
					}
				}else {
					Object o = constantPool.get(nextU2());
					if(o instanceof Long) {
						type = "LONG";
						value = o.toString() + "L";
					}else {
						type = "DOUBLE";
						value = o.toString();
					}
				}
				line = line.replace("TYPE", type).replace("VALUE", value);
			}else if(op==132) {	//iinc
				line = line.replace("U1_1", Integer.toString(nextU1())).replace("U1_2", Integer.toString(nextU1()));
			}else if(op==178||op==179) {	//static field
				int index = nextU2();
				String clazz = (String) constantPool.get((int) constantPool.get(((int[])constantPool.get(index))[0]));
				int[] nat = (int[]) constantPool.get(((int[])constantPool.get(index))[1]);
				String name = (String) constantPool.get(nat[0]);
				String desc = (String) constantPool.get(nat[1]);
				name = toCName(clazz)+"_static_"+name;
				char c = desc.charAt(0);
				switch(c) {
				case 'B': desc="BYTE"; break;
				case 'S': desc="SHORT"; break;
				case 'I': desc="INT"; break;
				case 'J': desc="LONG"; break;
				case 'F': desc="FLOAT"; break;
				case 'D': desc="DOUBLE"; break;
				case 'C': desc="CHAR"; break;
				case 'Z': desc="BOOLEAN"; break;
				default : desc="OBJECT"; break;
				}
				line = line.replace("TYPE", desc).replace("FIELD", name);
			}else if(op==180||op==181) {	//field
				int index = nextU2();
				String clazz = (String) constantPool.get((int) constantPool.get(((int[])constantPool.get(index))[0]));
				int[] nat = (int[]) constantPool.get(((int[])constantPool.get(index))[1]);
				String name = (String) constantPool.get(nat[0]);
				String desc = (String) constantPool.get(nat[1]);
				char c = desc.charAt(0);
				switch(c) {
				case 'B': desc="BYTE"; break;
				case 'S': desc="SHORT"; break;
				case 'I': desc="INT"; break;
				case 'J': desc="LONG"; break;
				case 'F': desc="FLOAT"; break;
				case 'D': desc="DOUBLE"; break;
				case 'C': desc="CHAR"; break;
				case 'Z': desc="BOOLEAN"; break;
				default : desc="OBJECT"; break;
				}
				line = line.replace("TYPE", desc).replace("FIELD", name).replace("CLASS", toCName(clazz));
			}else if(op>=182&&op<=185) {	//method invocation
				if(op!=184) {
					int index = nextU2();
					if(op==185) nextU2();
					String l = "opslot1.TYPE_RE = InvokeMethod(StackTop().OBJECT, INDEX, TYPE_FUNC, ARGS); DecStack(ARG_COUNT); PushSlot(opslot1);";
					int[] nat = ((int[])constantPool.get(((int[])constantPool.get(index))[1]));
					String name = (String) constantPool.get(nat[0]);
					String type = (String) constantPool.get(nat[1]);
					name+=type;
					if(!stringIds.contains(name)) stringIds.add(name);
					l = l.replace("INDEX", Integer.toString(stringIds.indexOf(name)));
					String[] decoded = decodeMethodDesc(type, false);
					l = l.replace("TYPE_RE", decoded[0]).replace("TYPE_FUNC", decoded[1]).replace("ARGS", decoded[2]).replace("ARG_COUNT", decoded[3]);
					line = line.replace("TO BE FILLED", l);
				}else {
					int index = nextU2();
					String l = "opslot1.TYPE_RE = CLASS_NAME(ARGS); DecStack(ARG_COUNT); PushSlot(opslot1);";
					int[] nat = ((int[])constantPool.get(((int[])constantPool.get(index))[1]));
					String name = (String) constantPool.get(nat[0]);
					String type = (String) constantPool.get(nat[1]);
					name+=type;
					String[] decoded = decodeMethodDesc(type, true);
					l = l.replace("TYPE_RE", decoded[0]).replace("ARGS", decoded[2]).replace("NAME", toCName(name)).replace("ARG_COUNT", decoded[3]).replace("CLASS", toCName((String)constantPool.get((int)constantPool.get(((int[])constantPool.get(index))[0]))));
					line = line.replace("TO BE FILLED", l);
				}
			}else if(op==187) {	//new
				int index = nextU2();
				line = line.replace("CLASS", toCName(getClass(index)));
			}else if(op==193) {
				int index = nextU2();
				line = line.replace("CLASS", toCName(getClass(index)));
			}else if(op==201) {	//U4
				line = line.replace("U4", Integer.toString(nextU4()));
			}else if(op==170||op==171) {	//switch
				if(op==170) {
					idx = (idx / 4 + 1) * 4;
					nextU4();
					int low = nextU4();
					int high = nextU4();
					idx += (high-low+1)*4;
				}else {
					idx = (idx / 4 + 1) * 4;
					nextU4();
					int npairs = nextU4();
					idx += (4 + 4 * npairs);
				}
			}
			code.append(line);
			} catch(Exception e) {}
		}
		code.append("}\n");
		
		idx = tarIdx;
		return code.toString();
	}
	
	private static boolean isMain = true;
	private void parseMain() {
		if(!isMain) return;
		main.append(toCName(getClass(clazz))+"_"+toCName("main([Ljava/lang/String;)V")+"(jnull);\n");
		isMain = false;
	}
	
	
	
	private int nextU1() {
		return code[idx++]<<24>>>24;
	}
	
	private int nextU2() {
		return ((code[idx++]<<24>>>16) + (code[idx++]<<24>>>24));
	}
	
	private int nextU4() {
		return (code[idx++]<<24>>>0) + (code[idx++]<<24>>>8) + (code[idx++]<<24>>>16) + (code[idx++]<<24>>>24);
	}
	
	private long nextU8() {
		long l = ByteBuffer.wrap(Arrays.copyOfRange(code, idx, idx+8)).getLong();
		idx+=8;
		return l;
	}
	
	private String nextString(int len) {
		String str = new String(Arrays.copyOfRange(code, idx, idx+len));
		idx+=len;
		return str;
	}
	
	
	
	private String getClass(int clazz) {
		if(clazz==0) return "zeechung/minijava/Object";
		return (String) constantPool.get((int) constantPool.get(clazz));
	}
	
	private String getUTF8(int str) {
		return (String) constantPool.get(str);
	}
	
	private String getString(int idx) {
		return getUTF8((int) constantPool.get(idx));
	}
	
	private long getLong(int i) {
		return (long) constantPool.get(i);
	}
	
	private float getFloat(int i) {
		return (float) constantPool.get(i);
	}
	
	private double getDouble(int i) {
		return (double) constantPool.get(i);
	}
	
	private int getInt(int i) {
		return (int) constantPool.get(i);
	}
	
	
	
	private boolean isStatic(int access) {
		if((0b00000000000000000000000000001000 & access) != 0) {
			return true;
		}
		return false;
	}
	
	private boolean isVirtual(int access) {
		if((0x00000400 & access) != 0) {
			return true;
		}
		return false;
	}
	
	private String[] decodeMethodDesc(String desc, boolean isStatic) {	//return TYPE_RE, TYPE_FUNC, ARGS, ARGS_COUNT
		String varDesc = desc.substring(desc.indexOf('(')+1, desc.indexOf(')'));
		String reDesc = desc.substring(desc.indexOf(')')+1);
		
		String typeRe = "";
		if(reDesc.length()!=1) {
			typeRe = "OBJECT";
		}else {
			char t = reDesc.charAt(0);
			if(t=='B') typeRe = "BYTE";
			else if(t=='S') typeRe = "SHORT";
			else if(t=='I') typeRe = "INT";
			else if(t=='J') typeRe = "LONG";
			else if(t=='F') typeRe = "FLOAT";
			else if(t=='D') typeRe = "DOUBLE";
			else if(t=='C') typeRe = "CHAR";
			else if(t=='Z') typeRe = "BOOLEAN";
			else typeRe = "INT";
		}
		
		String typeFunc = "RE(*)(ARGS)";
		String re = "";
		if(reDesc.length()!=1) {
			re = "jobject";
		}else {
			char t = reDesc.charAt(0);
			if(t=='B') re = "jbyte";
			else if(t=='S') re = "jshort";
			else if(t=='I') re = "jint";
			else if(t=='J') re = "jlong";
			else if(t=='F') re = "jfloat";
			else if(t=='D') re = "jdouble";
			else if(t=='C') re = "jchar";
			else if(t=='Z') re = "jboolean";
			else re = "jint";
		}
		
		StringBuffer args = new StringBuffer();
		StringBuffer varArgs = new StringBuffer();
		int argsCount = 0;
		if(!isStatic) {
			args.append("jobject this, ");
			varArgs.append("StackOffset("+(argsCount++)+").OBJECT,");
		}
		
		int argCount = 0;
		for(int i=0;i<varDesc.length();i++) {
			if(varDesc.charAt(i)=='[') {
				while(varDesc.charAt(i)=='[') i++;
				if(varDesc.charAt(i)!='L') {
					args.append("jobject arg"+(++argCount)+", ");
					varArgs.append("StackOffset("+(argsCount++)+").OBJECT,");
				}
				else {
					while(varDesc.charAt(i+1)!=';') i++;
					args.append("jobject arg"+(++argCount)+", ");
					varArgs.append("StackOffset("+(argsCount++)+").OBJECT,");
				}
			}else if(varDesc.charAt(i)=='L') {
				while(varDesc.charAt(i+1)!=';') i++;
				args.append("jobject arg"+(++argCount)+", ");
				varArgs.append("StackOffset("+(argsCount++)+").OBJECT,");
			}else {
				switch(varDesc.charAt(i)) {
				case 'B': args.append("jbyte arg"+(++argCount)+", "); varArgs.append("StackOffset("+(argsCount++)+").BYTE,"); break;
				case 'C': args.append("jchar arg"+(++argCount)+", "); varArgs.append("StackOffset("+(argsCount++)+").CHAR,"); break;
				case 'D': args.append("jdouble arg"+(++argCount)+", "); varArgs.append("StackOffset("+(argsCount++)+").DOUBLE,"); break;
				case 'F': args.append("jfloat arg"+(++argCount)+", "); varArgs.append("StackOffset("+(argsCount++)+").FLOAT,"); break;
				case 'I': args.append("jint arg"+(++argCount)+", "); varArgs.append("StackOffset("+(argsCount++)+").INT,"); break;
				case 'J': args.append("jlong arg"+(++argCount)+", "); varArgs.append("StackOffset("+(argsCount++)+").LONG,"); break;
				case 'S': args.append("jshort arg"+(++argCount)+", "); varArgs.append("StackOffset("+(argsCount++)+").SHORT,"); break;
				case 'Z': args.append("jboolean arg"+(++argCount)+", "); varArgs.append("StackOffset("+(argsCount++)+").BOOLEAN,"); break;
				}
			}
		}
		
		if((varDesc.length()!=0)||(!isStatic)) args.setCharAt(args.length()-2, ' ');
		
		typeFunc = typeFunc.replace("RE", re).replace("ARGS", args.toString());
		
		if(argsCount>=1) varArgs.setCharAt(varArgs.length()-1, ' ');
		
		return new String[] {typeRe, typeFunc, varArgs.toString(), ""+argsCount};
	}
	
	private String decodeMethodDesc(String name, String desc, boolean isStatic) {
		StringBuffer re = new StringBuffer();
		String varDesc = desc.substring(desc.indexOf('(')+1, desc.indexOf(')'));
		String reDesc = desc.substring(desc.indexOf(')')+1);
		
		if(reDesc.length()!=1) {
			re.append("jobject ");
		}else {
			switch(reDesc.charAt(0)) {
			case 'V': re.append("int "); break;
			case 'B': re.append("jbyte "); break;
			case 'C': re.append("jchar "); break;
			case 'D': re.append("jdouble "); break;
			case 'F': re.append("jfloat "); break;
			case 'I': re.append("jint "); break;
			case 'J': re.append("jlong "); break;
			case 'S': re.append("jshort "); break;
			case 'Z': re.append("jboolean "); break;
			}
		}
		
		re.append(name+toCName(desc));
		//re.append(name);
		re.append("(");
		if(!isStatic) {
			re.append("jobject this, ");
		}
		
		int argCount = 0;
		for(int i=0;i<varDesc.length();i++) {
			if(varDesc.charAt(i)=='[') {
				while(varDesc.charAt(i)=='[') i++;
				if(varDesc.charAt(i)!='L') re.append("jobject arg"+(++argCount)+", ");
				else {
					while(varDesc.charAt(i+1)!=';') i++;
					re.append("jobject arg"+(++argCount)+", ");
				}
			}else if(varDesc.charAt(i)=='L') {
				while(varDesc.charAt(i+1)!=';') i++;
				re.append("jobject arg"+(++argCount)+", ");
			}else {
				switch(varDesc.charAt(i)) {
				case 'B': re.append("jbyte arg"+(++argCount)+", "); break;
				case 'C': re.append("jchar arg"+(++argCount)+", "); break;
				case 'D': re.append("jdouble arg"+(++argCount)+", "); break;
				case 'F': re.append("jfloat arg"+(++argCount)+", "); break;
				case 'I': re.append("jint arg"+(++argCount)+", "); break;
				case 'J': re.append("jlong arg"+(++argCount)+", "); break;
				case 'S': re.append("jshort arg"+(++argCount)+", "); break;
				case 'Z': re.append("jboolean arg"+(++argCount)+", "); break;
				}
			}
		}
		
		if((varDesc.length()!=0)||(!isStatic)) re.setCharAt(re.length()-2, ')');
		else re.append(')');
		
		return re.toString();
	}
	
	private char[] decodeDescs(String desc, boolean isStatic) {
		
		desc = desc.substring(desc.indexOf('(')+1, desc.indexOf(')'));
		
		List<Character> re = new LinkedList<Character>();
		
		if(!isStatic) {
			re.add('L');
		}
		
		for(int i=0;i<desc.length();i++) {
			if(desc.charAt(i)=='[') {
				while(desc.charAt(i)=='[') i++;
				if(desc.charAt(i)!='L') re.add('L');
				else {
					while(desc.charAt(i+1)!=';') i++;
					re.add('L');
				}
			}else if(desc.charAt(i)=='L') {
				while(desc.charAt(i+1)!=';') i++;
				re.add('L');
			}else {
				switch(desc.charAt(i)) {
				case 'B': re.add('B'); break;
				case 'C': re.add('C'); break;
				case 'D': re.add('D'); break;
				case 'F': re.add('F'); break;
				case 'I': re.add('I'); break;
				case 'J': re.add('J'); break;
				case 'S': re.add('S'); break;
				case 'Z': re.add('Z'); break;
				}
			}
		}
		
		char[] c = new char[re.size()];
		for(int i=0;i<re.size();i++) {
			c[i] = re.get(i);
		}
		return c;
	}
	
	private String decodeDesc(String desc) {
		int i = -1;
		int arrDepth = 0;
		while(desc.charAt(++i)=='[') {
			arrDepth++;
		}
		StringBuffer strbfr = new StringBuffer();
		char type = desc.charAt(i);
		if(type=='L') {
			strbfr.append(desc.substring(i+1, desc.length()-1));
		}else if(type=='B') {
			strbfr.append("jbyte");
		}else if(type=='C') {
			strbfr.append("jchar");
		}else if(type=='D') {
			strbfr.append("jdouble");
		}else if(type=='F') {
			strbfr.append("jfloat");
		}else if(type=='I') {
			strbfr.append("jint");
		}else if(type=='J') {
			strbfr.append("jlong");
		}else if(type=='S') {
			strbfr.append("jshort");
		}else if(type=='Z') {
			strbfr.append("jboolean");
		}
		
		for(i=0;i<arrDepth;i++) {
			strbfr.append("[]");
		}
		return strbfr.toString();
	}
	
	private static String toCName(String jName) {
		return jName.replace("_", "_u_").replace("/", "_d_").replace(";", "_s_").replace("(", "_l_").replace(")", "_r_")
				.replace("<", "_ls_").replace(">", "_rs_").replace("\"", "_c_").replace("[", "_a_");
	}
}
