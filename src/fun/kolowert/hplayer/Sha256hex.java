package fun.kolowert.hplayer;

public class Sha256hex {

	public static void main(String[] args) {

		String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText);
		
		final String hashed = Hashing.sha256()
		        .hashString("your input", StandardCharsets.UTF_8)
		        .toString();

	}

}
