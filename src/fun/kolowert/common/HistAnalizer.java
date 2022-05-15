package fun.kolowert.common;

import java.util.Arrays;
import java.util.List;

import fun.kolowert.serv.Serv;

public class HistAnalizer {
	
	private final HistHandler histHandler;
	private final List<int[]> histCombinations;

	public HistAnalizer(GameType gameType, int histDeep, int histShift) {
		histHandler = new HistHandler(gameType, histDeep, histShift);
		histCombinations = histHandler.getHistCombinations();
	}

	public MatchingReport makeMatchingReport(int[] playCombination, String textUnit) {
		int[] analizResult = new int[1 + playCombination.length];
		for (int[] comb : histCombinations) {
			int matches = countMatches(playCombination, comb);
			analizResult[matches] += 1;
		}
		return new MatchingReport(playCombination, analizResult, textUnit);
	}

	/**
	 * It count matches balls in combination from parameter and balls in history
	 * combinations It takes history combinations from file in resources folder
	 * 
	 * @param ballSet  to analyze
	 * @param histDeep -> deep in history to analyze
	 * @return string report (like array of integers) with matches for 0, 1, 2, 3, 4
	 *         and more balls (depends on gameType)
	 */
	public String reportMatches(int[] ballSet) {
		int[] analizResult = new int[1 + ballSet.length];

		for (int[] comb : histCombinations) {
			int matches = countMatches(ballSet, comb);
			analizResult[matches] += 1;
		}

		return Serv.normalizeArray(analizResult, "[", "]");
	}

	private int countMatches(int[] a, int[] b) {
		int counter = 0;
		for (int x : a) {
			for (int y : b) {
				if (x == y) {
					++counter;
				}
			}
		}
		return counter;
	}
	
	// debugging
	public void displayHistCombinations() {
		for (int[] combination : histCombinations) {
			System.out.println(Arrays.toString(combination));
		}
	}
	
	public HistHandler getHistHandler() {
		return histHandler;
	}
	
	// debugging
	public static void main(String[] args) {
		System.out.println("Hello from HistAnalizer");
		int shift = 2;
		HistAnalizer ha = new HistAnalizer(GameType.KENO, 5, shift);
		// System.out.println(ha.reportMatches(new int[] { 1, 2, 3, 4, 5 }));
		ha.displayHistCombinations();
		System.out.println("\n" + Arrays.toString(ha.getHistHandler().getNextLineOfHistBlock(shift)));
	}
}
