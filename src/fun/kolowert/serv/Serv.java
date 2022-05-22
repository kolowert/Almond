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

	public static String normInt2(int n) {
		return n < 10 ? "0" + n : "" + n;
	}

	public static String normInt3(int n) {
		return n < 10 ? "00" + n : n < 100 ? "0" + n : "" + n;
	}

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

	public static String normDouble4(double d) {
		String s = "" + 0.0001 * (int) (10000.0 * (d + 0.00005));
		if (s.length() > 6) {
			return s.substring(0, 6);
		}
		int z = 6 - s.length();
		StringBuilder sb = new StringBuilder(s);
		for (int i = 0; i < z; i++) {
			sb.append("0");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		for (int y = 64; y < 1_000_000; y *= 16) {
			System.out.println(normIntX(y, 6, "#"));
		}
	}

}
