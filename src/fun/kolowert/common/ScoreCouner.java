package fun.kolowert.common;

public class ScoreCouner {

	private ScoreCouner() {
	}

	/**
	 * It counts score as 4 * combinations of pairs on matched amount; For once
	 * match score is 10 * 0.1 = 1; For matching twice score is 10 * 1 = 10;
	 * Example: for 4 match there are 6 combinations for 2 on 4, as result we have
	 * 10 * 6 = 60;
	 */
	public static int countScore(GameType gameType, int[] matching) {
		int result = 0;
		switch (gameType) {
		case MAXI:
			result = scoreCounter(new int[] { 0, 1, 10, 30, 60, 100 }, matching);
			break;
		case SUPER:
			result = scoreCounter(new int[] { 0, 1, 10, 30, 60, 100, 150 }, matching);
			break;
		case KENO:
			result = scoreCounter(
					new int[] { 0, 1, 10, 30, 60, 100, 150, 210, 280, 360, 450, 550, 660, 780, 910, 1050 }, matching);
			break;
		}
		return result;
	}

	// Servant for countScore(..)
	private static int scoreCounter(int[] mask, int[] matching) {
		int result = 0;
		for (int i = 1; i < matching.length && i < mask.length; i++) {
			result += matching[i] * mask[i];
		}
		return result;
	}

}
