package tk.mybatis.simple.util;

public class TestClass {
	public String name = "jls";
	
	static {
		System.out.println("this is static block");
	}
	
	public static void method() {
		System.out.println("this is static method");
	}
}
