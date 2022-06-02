package fun.kolowert.hplayer;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

public class Coder256Sha extends Coder {
	
	private static final String LINE = "!$0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	@Override
	public String makeLettered(String input, int outputLength) {
		String sha256 = makeSha256Code(input);
		return truncate(sha256, outputLength, LINE);
	}

	private String makeSha256Code(String input) {
		return Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
	}

}
