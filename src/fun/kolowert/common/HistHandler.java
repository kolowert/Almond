package fun.kolowert.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fun.kolowert.serv.FileHand;

public class HistHandler {

	private String path = "resources/hist/";
	private List<String> hist;
	List<int[]> histCombinations;

	public HistHandler(GameType gameType, int histDeep, int histShift) {
		path += gameType.getHistFileName();
		hist = new FileHand(path).read();
		histCombinations = convertToCombinations(hist, histDeep, histShift);
	}

	public List<int[]> getHistCombinations() {
		return histCombinations;
	}

	/**
	 * it gets histLine coming after histCombinations if is not any, it return last
	 * line
	 */
	public int[] getNextLineOfHistBlock(int histShift) {
		int lineIndex = histShift - 1 >= 0 ? histShift - 1 : 0;
		List<int[]> specialCombinations = convertToCombinations(hist, 1, lineIndex);
		return specialCombinations.get(0);
	}

	/**
	 * it parses this String line [2778,2022-05-20,?,1,5,11,22,27,44] 
	 * to that integer array [2778,5,11,22,27,44]
	 */
	private List<int[]> convertToCombinations(List<String> hist, int deep, int shift) {
		List<int[]> result = new ArrayList<>();

		for (int i = hist.size() - 1 - shift, counter = 0; i >= 0 && counter < deep; i--, counter++) {
			String line = hist.get(i);
			String[] parts = line.split(",");
			int len = parts.length - 3;
			int[] arr = new int[len];
			arr[0] = Integer.parseInt(parts[0]);
			for (int j = 1; j < len; j++) {
				try {
					arr[j] = Integer.parseInt(parts[j + 3]);
				} catch (NumberFormatException e) {
					arr[j] = 0;
				}
			}

			result.add(arr);
		}

		return result;
	}

	// debugging
	public static void main(String[] args) {
		HistHandler hh = new HistHandler(GameType.MAXI, 5, 1);

		List<int[]> hcs = hh.getHistCombinations();
		for (int[] hc : hcs) {
			System.out.println(Arrays.toString(hc));
		}
	}
}
