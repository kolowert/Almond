package fun.kolowert.hplayer;

public class CoderKolo95 extends Coder {

	private static final String SALT;
	private static final String LINE;

	static {
		StringBuilder sb = new StringBuilder(96);
		int ch = 32;
		while (ch < 64) {
			sb.append((char) ch);
			sb.append((char) (ch + 32));
			if (ch + 64 >= 127)
				break;
			sb.append((char) (ch + 64));
			++ch;
		}
		SALT = sb.toString();

		sb.delete(0, sb.length());
		ch = 32;
		while (ch < 127) {
			sb.append((char) ch++);
		}
		LINE = sb.toString();
	}

	public int makeInteger(String input) {
		return makeLettered(input, 16).hashCode();
	}
	
	@Override
	public String makeLettered(String input, int outputLength) {
		return makeLettered(input, outputLength, SALT, LINE);
	}

	// debugging
	public static void main(String[] args) {
		CoderKolo95 coder = new CoderKolo95();
		String salt = coder.getSalt();
		System.out.println("salt: " + salt + "\nlength: " + salt.length());
		String line = coder.getLine();
		System.out.println("line: " + line + "\nlength: " + line.length());
	}

	private String getSalt() {
		return SALT;
	}

	private String getLine() {
		return LINE;
	}
}
