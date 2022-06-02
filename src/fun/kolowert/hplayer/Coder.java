package fun.kolowert.hplayer;

public abstract class Coder {
	
	public abstract String makeLettered(String input, int outputLength);
	
	public String makeLettered(String input, int outputLength, String salt, String line) {
		String salted = mixWithSalt(input, outputLength, salt, line);
		String transponded1 = transpondOnLine(salted, line);
		String shifted1 = innerShift(transponded1, line);
		String transponded2 = transpondOnLine(shifted1, line);
		String shifted2 = innerShift(transponded2, line);
		return Coder.truncate(shifted2, outputLength, line);
	}

	public static String mixWithSalt(String input, int outputLength, String salt, String line) {
		int inputLen = input.length();
		int saltLen = salt.length();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < inputLen; i++) {
			sb.append(input.charAt(i));
			if (i % 3 == 0 && i < saltLen) {
				sb.append(salt.charAt(i));
			}
		}
		if (sb.length() < outputLength) {
			for (int i = outputLength - sb.length(); i > 0; i--) {
				int index = (i * 13) % line.length();
				sb.append(line.charAt(index));
			}
		}
		return sb.toString();
	}

	public static String transpondOnLine(String input, String line) {
		StringBuilder sb = new StringBuilder();
		int odd = Coder.findOdd(input);

		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			int code = (odd + ch) % line.length();
			char chx = line.charAt(code);
			sb.append(chx);
			odd += 13 + code;
			if (odd < 0) {
				odd = 13;
			}
		}
		return sb.toString();
	}

	public static String innerShift(String input, String line) {
		StringBuilder output = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			int charIndex = findCharIndex(ch, line);
			char chx = input.charAt(charIndex % input.length());
			output.append(chx);
		}
		return output.toString();
	}

	private static int findCharIndex(char ch, String line) {
		int result = 13 + ch;
		for (int i = 0; i < line.length(); i++) {
			if (ch == line.charAt(i)) {
				result = i;
				break;
			}
		}
		return result;
	}

	public static String truncate(String input, int len, String line) {
		StringBuilder sb = new StringBuilder(len);
		int odd = 3 * findOdd(input);
		for (int i = 0; i < len; i++) {
			odd -= input.charAt(i);
			if (odd < 0) {
				odd *= -1;
			}
			sb.append(line.charAt(odd % line.length()));
		}
		return sb.toString();
	}

	protected static int findOdd(String input) {
		int sum = 13;
		for (int i = 0; i < input.length(); i++) {
			sum += input.charAt(i);
		}
		return sum >= 0 ? sum : -1 * sum;
	}
}
