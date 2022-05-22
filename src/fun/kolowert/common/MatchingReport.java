package fun.kolowert.common;

import java.util.Arrays;

import fun.kolowert.serv.Serv;

public class MatchingReport {

	private final int[] playCombination;
	private final int[] matching;
	private final String textUnit;

	private int score;

	public MatchingReport(int[] playCombination, int[] matching, String textUnit) {
		super();
		this.playCombination = playCombination;
		this.matching = matching;
		this.textUnit = textUnit;
	}

	public String report() {
		return Serv.normalizeArray(playCombination) + "  " + Serv.normalizeArray(matching, "[", "]") 
				+ " " + Serv.normIntX(score, 4, " ") + "  " + textUnit;
	}
	
	public void makeScore(GameType gameType) {
		score = countScore(gameType, matching);
	}
	
	public static int countScore(GameType gameType, int[] matchingReport) {
		int result = 0;
		switch (gameType) {
		case SUPER:
			result = scoreCounter(new int[] { 0, 1, 16, 41, 1_100, 75_000, 1_000_000 }, matchingReport);
			break;
		case MAXI:
			result = scoreCounter(new int[] { 0, 1, 12, 150, 10_000, 400_000 }, matchingReport);
			break;
		case KENO:
			result = scoreCounter(new int[] { 0, 1, 8, 16, 32, 80, 200, 600, 4000, 40_000, 800_000 }, matchingReport);
			break;
		}
		return result;
	}
	
	// Servant for countScore(..)
	private static int scoreCounter(int[] mask, int[] matchingReport) {
		int result = 0;
		for (int i = 1; i < matchingReport.length && i < mask.length; i++) {
			result += matchingReport[i] * mask[i];
		}
		return result;
	}

	public int getScore() {
		return score;
	}

	public int[] getMatching() {
		return matching;
	}

	public int[] getPlayCombination() {
		return playCombination;
	}

	@Override
	public String toString() {
		return "MatchingReport [playCombination=" + Arrays.toString(playCombination) + ", matchin="
				+ Arrays.toString(matching) + ", textUnit=" + textUnit + "]";
	}

}
