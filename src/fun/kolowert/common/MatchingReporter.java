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
		for (int[] histComb : histCombinations) {
			int matches = countMatches(playCombination, histComb);
			matching[matches] += 1;
		}
		return new MatchingReport(gameType, playCombination, matching, textUnit);
	}

	private int countMatches(int[] playComb, int[] histComb) {
		int counter = 0;
		for (int playBall : playComb) {
			for (int i = 1; i < histComb.length; i++) {
				if (playBall == histComb[i]) {
					++counter;
				}
			}
		}
		return counter;
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
	
	// debugging
	private void displayHistCombinations() {
		for (int[] combination : histCombinations) {
			System.out.println(Arrays.toString(combination));
		}
	}
	
}
