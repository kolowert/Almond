package fun.kolowert.serv;

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

}
