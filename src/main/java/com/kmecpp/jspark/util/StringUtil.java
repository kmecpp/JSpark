package com.kmecpp.jspark.util;

import java.util.Arrays;

public class StringUtil {

	public static String repeat(char c, int times) {
		char[] chars = new char[times];
		Arrays.fill(chars, c);
		return new String(chars);
	}

}
