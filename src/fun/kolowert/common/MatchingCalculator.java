package fun.kolowert.common;

import java.util.List;

public class MatchingCalculator {
	
	private MatchingCalculator() {
	}
	
	public static int[] countMatches(int[] playCombination, List<int[]> histCombinations) {
		int[] matching = new int[1 + playCombination.length];
		for (int[] histCombination : histCombinations) {
			int matches = countMatches(playCombination, histCombination);
			matching[matches] += 1;
		}
		return matching;
	}

	private static int countMatches(int[] playComb, int[] histComb) {
		int counter = 0;
		for (int playBall : playComb) {
			for (int i = 1; i < histComb.length; i++) {
				if (playBall == histComb[i]) {
					++counter;
					continue;
				}
			}
		}
		return counter;
	}
	
}
