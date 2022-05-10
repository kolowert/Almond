package fun.kolowert.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fun.kolowert.serv.FileHand;
import fun.kolowert.serv.Serv;

public class HistAnalizer {

	private String path = "resources/hist/";
	private List<String> hist;
	List<int[]> histCombinations;

	public HistAnalizer(GameType gameType, int histDeep, int histShift) {
		path += gameType.getHistFileName();
		hist = new FileHand(path).read();
		histCombinations = convertToCombinations(hist, histDeep, histShift);
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
	
	/** here we are */
	private List<int[]> convertToCombinations(List<String> hist, int deep, int shift) {
		List<int[]> result = new ArrayList<>();

		for (int i = hist.size() - 1 - shift, counter = 0; i >= 0 && counter < deep; i--, counter++) {
			String line = hist.get(i);
			String[] parts = line.split(",");
			int len = parts.length - 4;
			int[] arr = new int[len];

			for (int j = 0; j < len; j++) {
				try {
					arr[j] = Integer.parseInt(parts[j + 4]);
				} catch (NumberFormatException e) {
					arr[j] = 0;
				}
			}

			result.add(arr);
		}
		// System.out.println("convertToCombinations#HistAnalyzer shift:" + shift + " result.size:" + result.size()); // debugging
		return result;
	}
	
	/**
	 * it gets histLine coming after histCombinations
	 * if is not any, it return last line  
	 */
	public int[] getNextLineOfHistBlock(int histShift) {
		int lineIndex = histShift - 1 >= 0 ? histShift - 1 : 0; 
		List<int[]> specialCombinations = convertToCombinations(hist, 1, lineIndex);
		return specialCombinations.get(0);
	}
	
	// debugging
	public void displayHistCombinations() {
		for (int[] combination : histCombinations) {
			System.out.println(Arrays.toString(combination));
		}
	}
	
	// debugging
	public static void main(String[] args) {
		System.out.println("Hello from HistAnalizer");
		int shift = 2;
		HistAnalizer ha = new HistAnalizer(GameType.KENO, 5, shift);
		// System.out.println(ha.reportMatches(new int[] { 1, 2, 3, 4, 5 }));
		ha.displayHistCombinations();
		System.out.println("\n" + Arrays.toString(ha.getNextLineOfHistBlock(shift)));
	}
}
