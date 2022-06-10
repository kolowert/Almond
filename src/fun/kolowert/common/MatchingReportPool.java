package fun.kolowert.common;

import java.util.ArrayList;
import java.util.List;

import fun.kolowert.serv.Serv;

public class MatchingReportPool {

	private final GameType gameType;

	private List<int[]> playCombinations;
	private List<int[]> histCombinations;
	private List<int[]> matchings;
	private List<Integer> scores;
	private List<String> textUnits;

	public MatchingReportPool(GameType gameType, List<int[]> histCombs, int poolInitSize) {
		this.gameType = gameType;
		playCombinations = new ArrayList<>(poolInitSize);
		histCombinations = histCombs;
		matchings = new ArrayList<>(poolInitSize);
		scores = new ArrayList<>(poolInitSize);
		textUnits = new ArrayList<>(poolInitSize);
	}
	
	public void addRecord(int[] playCombination,int[] matching, String textUnit) {
		playCombinations.add(playCombination);
		matchings.add(matching);
		scores.add(ScoreCouner.countScore(gameType, matching));
		textUnits.add(textUnit);
	}
	
	public void addRecord(int[] playCombination, String textUnit) {
		playCombinations.add(playCombination);
		int[] matching = MatchingCalculator.countMatches(playCombination, histCombinations);
		matchings.add(matching);
		scores.add(ScoreCouner.countScore(gameType, matching));
		textUnits.add(textUnit);
	}
	
	public String report() {
		StringBuilder sb = new StringBuilder(playCombinations.size());
		if (playCombinations.isEmpty()) {
			return "Zero Lines in Report!";
		}
		int combSetSize = playCombinations.get(0).length;
		int gameSetSize = gameType.getGameSetSize();
		
		for (int i = 0; i < playCombinations.size(); i++) {
			sb	.append(Serv.normIntX(i + 1, 3, " ")).append(" ")
				.append("  ").append(combSetSize).append("/").append(gameSetSize).append("\t")
				.append(Serv.normalizeArray(playCombinations.get(i))).append("  ")
				.append(Serv.normalizeArray(matchings.get(i), "[", "]")).append(" ")
				.append(Serv.normIntX(scores.get(i), 4, " ")).append(" ")
				.append(textUnits.get(i))
				.append(System.lineSeparator());
			
		}
		
		return sb.toString().substring(0, sb.length() - 1);
	}
	
	public String report(int index) {
		StringBuilder sb = new StringBuilder(96);
		int combSetSize = playCombinations.get(0).length;
		int gameSetSize = gameType.getGameSetSize();
		sb	.append(Serv.normIntX(index + 1, 3, " ")).append(" ")
			.append("  ").append(combSetSize).append("/").append(gameSetSize).append("\t")
			.append(Serv.normalizeArray(playCombinations.get(index))).append("  ")
			.append(Serv.normalizeArray(matchings.get(index), "[", "]")).append(" ")
			.append(Serv.normIntX(scores.get(index), 4, " ")).append(" ")
			.append(textUnits.get(index));
		return sb.toString();
	}
	
	public GameType getGameType() {
		return gameType;
	}
	
	public List<int[]> getPlayCombinations() {
		return playCombinations;
	}

	public List<int[]> getMatchings() {
		return matchings;
	}

	public List<Integer> getScores() {
		return scores;
	}

	public List<String> getTextUnits() {
		return textUnits;
	}
	public int size() {
		return playCombinations.size();
	}
		
}
