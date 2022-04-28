package fun.kolowert.hplayer;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

public class Coder256Sha implements Coder {
	
	private static final String LINE = "!$0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	@Override
	public String makeLettered(String input, int outputLength) {

		String sha256 = makeSha256Code(input);

		return truncate(sha256, outputLength);
	}

	private String makeSha256Code(String input) {
		return Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
	}

	private String truncate(String input, int len) {
		StringBuilder sb = new StringBuilder(len);
		int odd = 2 * findOdd(input);
		for (int i = 0; i < len; i++) {
			odd -= input.charAt(i);
			sb.append(LINE.charAt(odd % LINE.length()));
		}
		return sb.toString();
	}

	private int findOdd(String input) {
		int sum = 13;
		for (int i = 0; i < input.length(); i++) {
			sum += input.charAt(i);
		}
		return sum >= 0 ? sum : -1 * sum;
	}

}
