package com.kmecpp.jspark;

import java.util.ArrayList;

public class JSparkMath {

	public static int[] divisors(int n) {
		//		long start = System.nanoTime();
		ArrayList<Integer> divisors = new ArrayList<>();
		double sqrt = Math.sqrt(n);
		for (int i = 1; i <= sqrt; i++) {
			if (n % i == 0) {
				divisors.add(i);
			}
		}
		int half = divisors.size() - (sqrt % 1 == 0 ? 1 : 0);
		for (int i = 0; i < half; i++) {
			divisors.add(n / divisors.get(half - i - 1));
		}
		int[] ints = new int[divisors.size()];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = divisors.get(i);
		}
		//		System.out.println("Time Taken: " + (System.nanoTime() - start) / 1000000F + "ms");
		return ints;
	}

	public static boolean isPrime(int n) {
		if (n % 2 == 0) {
			return false;
		}
		for (int i = 3; i <= Math.sqrt(n); i += 3) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

}
