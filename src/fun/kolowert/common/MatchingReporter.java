package fun.kolowert.common;

import java.util.Arrays;
import java.util.List;

public class MatchingReporter {
	
	private final GameType gameType;
	private final HistHandler histHandler;
	private final List<int[]> histCombinations;

	public MatchingReporter(GameType gameType, int histDeep, int histShift) {
		this.gameType = gameType;
		histHandler = new HistHandler(gameType, histDeep, histShift);
		histCombinations = histHandler.getHistCombinations();
	}

	public MatchingReport makeMatchingReport(int[] playCombination, String textUnit) {
		int[] matching = new int[1 + playCombination.length];
		for (int[] comb : histCombinations) {
			int matches = countMatches(playCombination, comb);
			matching[matches] += 1;
		}
		return new MatchingReport(gameType, playCombination, matching, textUnit);
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
		System.out.println("Hello from MatchingReporter");
		int shift = 2;
		MatchingReporter mr = new MatchingReporter(GameType.KENO, 5, shift);
		// System.out.println(mr.reportMatches(new int[] { 1, 2, 3, 4, 5 }));
		mr.displayHistCombinations();
		System.out.println("\n" + Arrays.toString(mr.getHistHandler().getNextLineOfHistBlock(shift)));
	}
}
