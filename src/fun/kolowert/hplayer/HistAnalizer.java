package fun.kolowert.hplayer;

import java.util.ArrayList;
import java.util.List;

public class HistAnalizer {

	private String path = "resources/hist/";

	public HistAnalizer(GameType gameType) {
		path += gameType.histFile();
	}

	/**
	 * It count matches balls in combination from parameter and balls in history
	 * combinations It takes history combinations from file in resources folder
	 * 
	 * @param ballSet to analyze
	 * @return string report (like array of integers) with matches for 0, 1, 2, 3, 4
	 *         and 5 balls
	 */
	public String reportMatches(int[] ballSet) {

		List<String> hist = new FileHand(path).read();

		List<int[]> histCombinations = convertToCombinations(hist);

		int[] analizResult = new int[6];
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

	private List<int[]> convertToCombinations(List<String> hist) {
		List<int[]> result = new ArrayList<>();
		for (String line : hist) {
			String[] parts = line.split(",");
			int[] arr = new int[5];
			for (int i = 0; i < 5; i++) {
				try {
					arr[i] = Integer.parseInt(parts[i + 4]);
				} catch (NumberFormatException e) {
					arr[i] = 0;
				}
			}
			result.add(arr);
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println("Hello from HistAnalizer");
		System.out.println(new HistAnalizer(GameType.MAXI).reportMatches(new int[] { 1, 2, 3, 4, 5 }));
	}
}
