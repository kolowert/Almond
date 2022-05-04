package fun.kolowert.hplayer;

import java.util.ArrayList;
import java.util.List;

public class HistAnalizer {

	private String path = "resources/hist/";

	public HistAnalizer(GameType gameType) {
		path += gameType.getHistFileName();
	}

	public MatchingReport makeMatchingReport(int[] playCombination, int histDeep, String textUnit) {
		List<String> hist = new FileHand(path).read();
		List<int[]> histCombinations = convertToCombinations(hist, histDeep);
		
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
	public String reportMatches(int[] ballSet, int histDeep) {

		List<String> hist = new FileHand(path).read();

		List<int[]> histCombinations = convertToCombinations(hist, histDeep);

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

	private List<int[]> convertToCombinations(List<String> hist, int deep) {

		List<int[]> result = new ArrayList<>();

		for (int i = hist.size() - 1, counter = 0; i >= 0 && counter < deep; i--, counter++) {
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

		return result;
	}

	public static void main(String[] args) {
		System.out.println("Hello from HistAnalizer");
		System.out.println(new HistAnalizer(GameType.SUPER).reportMatches(new int[] { 1, 2, 3, 4, 5 }, 10));
	}
}
