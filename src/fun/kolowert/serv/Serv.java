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

}
