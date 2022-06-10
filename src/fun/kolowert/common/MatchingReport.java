package fun.kolowert.common;

import java.util.Arrays;

import fun.kolowert.serv.Serv;

public class MatchingReport {
	
	private final int[] playCombination;
	private final int[] matching;
	private final int score;
	private final String textUnit;

	public MatchingReport(GameType gameType, int[] playCombination, int[] matching, String textUnit) {
		this.playCombination = playCombination;
		this.matching = matching;
		score = ScoreCouner.countScore(gameType, matching);
		this.textUnit = textUnit;
	}

	public String report() {
		return Serv.normalizeArray(playCombination) + "  " + Serv.normalizeArray(matching, "[", "]") 
				+ " " + Serv.normIntX(score, 4, " ") + "  " + textUnit;
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
