package fun.kolowert.aplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingCalculator;
import fun.kolowert.common.ScoreCouner;
import fun.kolowert.serv.Serv;

public class Reporter {

	private final GameType gameType;
	private final List<int[]> histBox;
	private final int[] nextLine;

	private List<BigCombination> bigCombinations;
	private final int combinationsAmountLimit;
	private int minimalScore;

	public Reporter(GameType gameType, List<int[]> histBox, int[] nextLine, int combinationsAmountLimit) {
		this.gameType = gameType;
		this.histBox = histBox;
		this.nextLine = nextLine;
		this.bigCombinations = new ArrayList<>(combinationsAmountLimit + 1);
		this.combinationsAmountLimit = combinationsAmountLimit;
		minimalScore = Integer.MAX_VALUE;
	}

	public void processCombination(int[] combination) {
		int[] matching = MatchingCalculator.countMatches(combination, histBox);
		int score = ScoreCouner.countScore(gameType, matching);
		BigCombination bigCombination = new BigCombination(combination, matching, score);

		if (bigCombinations.size() < combinationsAmountLimit) {
			// while list is not full add each..
			bigCombinations.add(bigCombination);
			// .. and renew minimal score
			if (minimalScore > score) {
				minimalScore = score;
			}
		} else {
			// when list is full add only if score is big enough
			if (score > minimalScore) {
				bigCombinations.add(bigCombination);
				// .. and rid off line with the smallest score
				Collections.sort(bigCombinations);
				minimalScore = bigCombinations.get(1).getScore();
				bigCombinations.remove(0);
			}
		}

	}

	/**
	 * It returns quantity of ball-popping in format DD.dd where DD is quantity and
	 * .dd is ball-id divided by 100.0
	 */
	public double[] countFrequencyes() {
		// Count Frequency of balls
		int[] counter = new int[gameType.getGameSetSize() + 1];

		for (BigCombination bigCombination : bigCombinations) {
			int[] combination = bigCombination.getCombination();
			for (int ball : combination) {
				++counter[ball];
			}
		}
		// prepare report
		double[] freqReport = new double[gameType.getGameSetSize() + 1];
		for (int i = 1; i < counter.length; i++) {
			freqReport[i] = counter[i] + 0.01 * i;
		}
		Arrays.sort(freqReport);
		return freqReport;
	}

	public static String reportFrequencyes(double[] frequencyis) {
		StringBuilder sb = new StringBuilder(6 * frequencyis.length);
		for (int i = frequencyis.length - 1; i >= 0; i--) {
			int order = (int) frequencyis[i];
			int preball = (int) (100 * (frequencyis[i] - order) + 0.005);
			String ball = Serv.normIntX(preball, 2, "0");
			sb.append(order).append("(").append(ball).append(")").append(" ");
		}
		
		return sb.toString();
	}
	
	public static int[] extractBallSequence(double[] frequencyis) {
		int[] result = new int[frequencyis.length - 1];
		for (int j = 1, i = frequencyis.length - 2; i >= 0; j++, i--) {
			int order = (int) frequencyis[j];
			result[i] = (int) (100 * (frequencyis[j] - order) + 0.005);
		}
		return result;
	}

	public String reportPlayCombinations() {
		StringBuilder sb = new StringBuilder(bigCombinations.size() + 4);
		if (bigCombinations.isEmpty()) {
			return "Zero Lines in Report!";
		}
		int combSetSize = bigCombinations.get(0).getCombinationLength();
		int gameSetSize = gameType.getGameSetSize();

		for (int i = 0; i < bigCombinations.size(); i++) {
			sb.append(Serv.normIntX(i + 1, 3, " ")).append(" ").append("  ").append(combSetSize).append("/")
					.append(gameSetSize).append("\t").append(bigCombinations.get(i).report()).append("  ")
					.append(System.lineSeparator());

		}

		return sb.toString().substring(0, sb.length() - 1);
	}

	public int[] getNextLine() {
		return nextLine;
	}
}
