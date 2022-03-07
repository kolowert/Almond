package fun.kolowert.hplayer;

public class HashCoder {

	private static final String SALT = "NhYrLkNgUwIaSoLpFnHyRlKxGuWiAsOmPNDzCcbHRdlscKExxLDKGupEykqQXDBH";
	private static final String LINE = "$&0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	private HashCoder() {
	}

	public static int makeInteger(String input) {
		return makeLettered(input, 16).hashCode();
	}

	public static String makeLettered(String input, int outputLength) {
		String salted = HashCoder.mixWithSalt(input, outputLength);
		String transponded1 = HashCoder.transpondOnLine(salted);
		String shifted1 = HashCoder.innerShift(transponded1);
		String transponded2 = HashCoder.transpondOnLine(shifted1);
		String shifted2 = HashCoder.innerShift(transponded2);
		return HashCoder.truncate(shifted2, outputLength);
	}

	public static String innerShift(String input) {
		StringBuilder output = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			int charIndex = findCharIndex(ch);
			char chx = input.charAt(charIndex % input.length());
			output.append(chx);
		}
		return output.toString();
	}
	
	private static int findCharIndex(char ch) {
		int result = 13 + ch;
		for (int i = 0; i < LINE.length(); i++) {
			if (ch == LINE.charAt(i)) {
				result = i;
				break;
			}
		}
		return result;
	}

	public static String transpondOnLine(String input) {

		StringBuilder sb = new StringBuilder();
		int odd = findOdd(input);

		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			int code = (odd + ch) % LINE.length();
			char chx = LINE.charAt(code);
			sb.append(chx);
			odd += 3;
		}

		return sb.toString();
	}

	public static String mixWithSalt(String input, int outputLength) {
		int inputLen = input.length();
		int saltLen = SALT.length();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < inputLen; i++) {
			sb.append(input.charAt(i));
			if (i < saltLen) {
				sb.append(SALT.charAt(i));
			}
		}
		
		if (sb.length() < outputLength) {
			for (int i = outputLength - sb.length(); i > 0; i--) {
				int index = (i * 13) % LINE.length();
				sb.append(LINE.charAt(index));
			}
		}
		
		return sb.toString();
	}
	
	public static String truncate(String input, int len) {
		StringBuilder sb = new StringBuilder(len);
		int odd = 2 * findOdd(input);
		for (int i = 0; i < len; i++) {
			odd -= input.charAt(i);
			sb.append(LINE.charAt(odd % LINE.length()));
		}
		return sb.toString();
	}
	
	private static int findOdd(String input) {
		int sum = 13;
		for (int i = 0; i < input.length(); i++) {
			sum += input.charAt(i);
		}
		return sum >=0 ? sum : -1 * sum;
	}
	
}
