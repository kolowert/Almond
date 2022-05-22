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

	private static int countMatches(int[] a, int[] b) {
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
	
}
