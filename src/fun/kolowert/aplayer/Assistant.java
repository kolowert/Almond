package fun.kolowert.aplayer;

import java.util.ArrayList;
import java.util.List;

import fun.kolowert.serv.Serv;

public class Assistant {
	
	private Assistant() {}
	
	public static List<int[]> calculateHitsOnRenges(List<int[]> tab, int[] mask) {
		List<int[]> result = new ArrayList<>();

		for (int[] line : tab) {
			int[] sectorSums = new int[mask.length];
			for (int maskIndex = 0; maskIndex < mask.length; maskIndex++) {
				for (int i = 0; i < line.length; i++) {
					if (i < mask[maskIndex]) {
						sectorSums[maskIndex] += line[i];
					}
				}
			}
			int backSum = 0;
			for (int i = 1; i < sectorSums.length; i++) {
				backSum += sectorSums[i - 1];
				sectorSums[i] = sectorSums[i] - backSum;
			}
			result.add(sectorSums);
		}
		return result;
	}
	
	public static void displayTab(List<int[]> tab) {
		StringBuilder result = new StringBuilder();
		int counter = 0;
		for (int[] line : tab) {
			StringBuilder h = new StringBuilder(Serv.normIntX(++counter, 3, "0") + " | ");
			for (int i = 0; i < line.length; i++) {
				h.append(" ").append(Serv.normIntX(line[i], 3, " ")).append("  : ");
			}
			result.append(h).append(System.lineSeparator());
		}
		System.out.println(Serv.cut(result.toString(), 1));
	}
	
	public static void displayRangesHead(int[] mask) {
		StringBuilder sb = new StringBuilder();
		int left = 1;
		for (int i = 0; i < mask.length; i++) {
			sb.append(Serv.normIntX(left, 2, "0")).append("-").append(Serv.normIntX(mask[i], 2, "0")).append(" | ");
			left = mask[i] + 1;
		}
		System.out.println("mask| " + sb);
	}
	
	public static void displayHitsOnRangesResume(List<int[]> inputTab, int[] mask) {
		// Sum
		int[] sumLine = new int[mask.length];
		for (int[] line : inputTab) {
			for (int i = 0; i < line.length && i < sumLine.length; i++) {
				sumLine[i] += line[i];
			}
		}
		List<int[]> wrapingList = new ArrayList<>();
		wrapingList.add(sumLine);
		displayTab(wrapingList);
		
		// Coefficient
		double[] coef = new double[mask.length];
		int size = inputTab.size();
		for (int i = 0; i < sumLine.length && i < mask.length; i++) {
			coef[i] = 1.0 * sumLine[i] / size;
		}
		// Display Coefficient line
		StringBuilder sb = new StringBuilder("coef| ");
		for (int i = 0; i < coef.length; i++) {
			sb.append(Serv.normDoubleX(coef[i], 3)).append(" | ");
		}
		System.out.println(sb);
	}
	
}
