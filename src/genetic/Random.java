package genetic;

import java.security.SecureRandom;

public class Random {

	private static SecureRandom random = new SecureRandom();
	
	/**
	 * 
	 * @param min   inclusive
	 * @param max   exclusive
	 * @return a number between (min, max-1)
	 * @throws exceptions   but nothing useful, be careful to use properly
	 */
	public static int rangeInt(int min, int max) {
		return (int)(random.nextInt(max-min)+min);
	}
	
	public static int nextInt(int n) {
		return random.nextInt(n);
	}
	
	public static boolean nextBoolean() {
		return nextBoolean(0.5f);
	}
	
	public static boolean nextBoolean(float f) {
		if (random.nextFloat() < f)
			return true;
		else
			return false;
	}

	public static float nextFloat() {
		return random.nextFloat();
	}
}
