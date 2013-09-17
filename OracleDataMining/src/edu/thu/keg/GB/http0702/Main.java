package edu.thu.keg.GB.http0702;

import java.util.Map;
import java.util.TreeMap;

class A {
	Integer i;
	Float f;
	Long l;
	Double d;

	public A() {
		System.out.println("1");
	}
	public A(int i) {
		this();
	}
}

public class Main extends A {
	
	static void proc(int sel) throws ArithmeticException,
			ArrayIndexOutOfBoundsException {
		System.out.println("in Situation" + sel);
		if (sel == 0) {
			System.out.println("no exception");

		} else if (sel == 1) {
			int i[] = new int[4];
			i[3] = 3;
		}
	}

	public static void main(String arg[]) {
		new A(1);
		int a = 2;
		double b = 5.0f;
		char c = 'e';
		boolean d = true;
		System.out.println("a=" + a + " b=" + b + " c=" + c + " d=" + d);
		System.out.println(0xA5);
		try {
			proc(0);
			proc(1);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("catch" + e);
		} finally {
			System.out.println("finally");
		}
		// System.out.println(m);
	}
}
