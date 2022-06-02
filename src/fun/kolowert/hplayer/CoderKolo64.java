package fun.kolowert.hplayer;

public class CoderKolo64 extends Coder {

	private static final String SALT = "ThEqUiCkBrOwNfxJuMpSoVeRtHLaZyDGQIcKbWnFXjmPsvlAzYdg 0123456789!";
	private static final String LINE = "!$0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public int makeInteger(String input) {
		return makeLettered(input, 16).hashCode();
	}
	
	@Override
	public String makeLettered(String input, int outputLength) {
		return makeLettered(input, outputLength, SALT, LINE);
	}

}
