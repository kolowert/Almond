package fun.kolowert.common;

import java.util.Arrays;

import fun.kolowert.serv.Serv;

public class MatchingReport {
	
	private final GameType gameType;
	private final int[] playCombination;
	private final int[] matching;
	private final int score;
	private final String textUnit;

	public MatchingReport(GameType gameType, int[] playCombination, int[] matching, String textUnit) {
		this.gameType = gameType;
		this.playCombination = playCombination;
		this.matching = matching;
		score = countScore();
		this.textUnit = textUnit;
	}

	public String report() {
		return Serv.normalizeArray(playCombination) + "  " + Serv.normalizeArray(matching, "[", "]") 
				+ " " + Serv.normIntX(score, 4, " ") + "  " + textUnit;
	}
	
	public int countScore() {
		int result = 0;
		switch (gameType) {
		case SUPER:
			result = scoreCounter(new int[] { 0, 1, 16, 41, 1_100, 75_000, 1_000_000 });
			break;
		case MAXI:
			result = scoreCounter(new int[] { 0, 1, 12, 150, 10_000, 400_000 });
			break;
		case KENO:
			result = scoreCounter(new int[] { 0, 1, 8, 16, 32, 80, 200, 600, 4000, 40_000, 800_000 });
			break;
		}
		return result;
	}
	
	// Servant for countScore(..)
	private int scoreCounter(int[] mask) {
		int result = 0;
		for (int i = 1; i < matching.length && i < mask.length; i++) {
			result += matching[i] * mask[i];
		}
		return result;
	}

	public int[] getPlayCombination() {
		return playCombination;
	}
	public int[] getMatching() {
		return matching;
	}

	public int getScore() {
		return score;
	}
	
	@Override
	public String toString() {
		return "MatchingReport [playCombination=" + Arrays.toString(playCombination) + ", matchin="
				+ Arrays.toString(matching) + ", textUnit=" + textUnit + "]";
	}

}
