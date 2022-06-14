package fun.kolowert.serv;

import fun.kolowert.common.GameType;

public class Serv {

	private Serv() {
	}

	public static String normalizeArray(int[] arr, String leftBracket, String rightBracket) {
		StringBuilder sb = new StringBuilder(leftBracket + " ");
		for (int n : arr) {
			if (n < 10) {
				sb.append(" ").append(n);
			} else {
				sb.append(n);
			}
			sb.append(", ");
		}
		return sb.toString().substring(0, sb.length() - 2) + " " + rightBracket;
	}

	public static String normalizeArray(int[] arr) {
		return normalizeArray(arr, "< ", " >");
	}
	
	/**
	 * @param number to norm
	 * @param length for normed result
	 * @param placeHolder before number
	 * @return String like "##ddd" where # - placeholder; d - digit of number
	 */
	public static String normIntX(int n, int length, String placeHolder) {
		String s = String.valueOf(n);
		int y = length - s.length();
		StringBuilder sb = new StringBuilder();
		while (y > 0) {
			sb.append(placeHolder);
			--y;
		}
		sb.append(s);
		return sb.toString();
	}
	
	public static String normDoubleX(double d, int x) {
		int n = (int) d;
		String sFraction = String.valueOf(d - n) + "0000000000000";
		return String.valueOf(n) + sFraction.substring(1, 2 + x);
	}
	
	public static void main(String[] args) {
		for (double y = 3.1415; y < 1_000_000; y *= 10.0) {
			System.out.println(normDoubleX(y, 2));
		}
	}
	
	public static String displayPlainHead(GameType gameType, String prefix) {
		StringBuilder sb = new StringBuilder(prefix);
		for (int i = 1; i <= gameType.getGameSetSize(); i++) {
			sb.append(Serv.normIntX(i, 2, "0")).append("|");
		}
		return sb.toString();
	}
	
	public static String displayIntArray(int[] input, String prefix) {
		return displayIntArray(input, prefix, 2, " ", ":", true);
	}
	
	public static String displayIntArray(int[] input, String prefix, int fildSize, String placeHolder, 
			String columnSeparator, boolean isEmptyWhenZero) {
		StringBuilder sb = new StringBuilder(prefix);
		for (int i = 0; i < input.length; i++) {
			if (input[i] == 0) {
				sb.append("  ").append(columnSeparator);
			} else {
				sb.append(Serv.normIntX(input[i], fildSize, placeHolder)).append(columnSeparator);
			}
		} 
		return sb.toString();
	}
	
	public static String displayIntArray(int[] input, int[] rangesMask, String prefix) {
		return displayIntArray(input, rangesMask, prefix, 2, " ", ":");
	}
	
	public static String displayIntArray(int[] input, int[] rangesMask, String prefix, int fildSize, String placeHolder, 
				String columnSeparator) {
		StringBuilder sb = new StringBuilder(prefix);
		String currentColumnSeparator;
		for (int i = 0; i < input.length; i++) {
			if (isInArray(i + 1, rangesMask)) 	{ currentColumnSeparator = "!"; }
			else 							{ currentColumnSeparator = columnSeparator; }
			if (input[i] == 0) {
				sb.append("  ").append(currentColumnSeparator);
			} else {
				sb.append(Serv.normIntX(input[i], fildSize, placeHolder)).append(currentColumnSeparator);
			}
		} 
		return sb.toString();
	}
	
	// servant for displayIntArray(..)
	private static boolean isInArray(int n, int[] arr) {
		for (int a : arr) {
			if (a == n) { return true; }
		}
		return false;
	}
	
	public static String cut(String input, int tail) {
		int len = input.length();
		if (tail > len + 1) { tail = len + 1; }
		return input.substring(0, len - tail - 1);
	}
}
