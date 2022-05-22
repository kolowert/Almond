package fun.kolowert.cplayer;

import java.util.ArrayList;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReporter;
import fun.kolowert.serv.Serv;

public class HitReporterSingle {
	
	public double[] makeHitReports(GameType gameType, int histDeep, int histShift, double[] frequencyReport) {
		int[] bigSizes = { 10, 20, 30, 40, 50, 60, 70, 80 };
		int smallFreq = 5;
		
		double[] result = new double[bigSizes.length + 1];
		
		MatchingReporter histAnalyzer = new MatchingReporter(gameType, histDeep, histShift);
		int[] nextLineOfHistBlock = histAnalyzer.getHistHandler().getNextLineOfHistBlock(histShift);
		
		for (int ind = 0; ind < bigSizes.length; ind++) {
			int[] bigFrequencySet = makeBigFrequencySet(frequencyReport, bigSizes[ind]);
			int bigHitCount = countHits(bigFrequencySet, nextLineOfHistBlock);
			result[ind] = bigHitCount + 0.01 * bigFrequencySet.length;
		}
		
		int[] smallFrequencySet = makeSmallFrequencySet(frequencyReport, smallFreq);
		int smallHitCount = countHits(smallFrequencySet, nextLineOfHistBlock);
		result[bigSizes.length] = smallHitCount + 0.01 * smallFrequencySet.length;

		return result;
	}

	public String reportHitReports(double[] hitReports) {
		StringBuilder sb = new StringBuilder();
		for (double r : hitReports) {
			int hitCount = (int) r;
			int setSize = (int) (0.5 + 100 * (r - (int) r));
			double hitCoef = 1.0 * hitCount / setSize;
			String bigHitReport = String.format(" %d/%d-%s ", hitCount, setSize, Serv.normDouble4(hitCoef));
			sb.append(bigHitReport).append("  ");
		}
		return sb.toString();
	}
	
	public String reportIsolatedHitReports(double[] hitReports) {
		StringBuilder sb = new StringBuilder();
		int hitCounts = 0;
		int setSizes = 0;
		for (int i = 0; i < hitReports.length; i++) {
			int hitCount = (int) hitReports[i];
			int setSize = (int) (0.5 + 100 * (hitReports[i] - (int) hitReports[i]));
			
			// isolation
			hitCount -= hitCounts;
			setSize -= setSizes;
			hitCounts += hitCount;
			setSizes += setSize;
			
			double hitCoef = 1.0 * hitCount / setSize;
			String bigHitReport = String.format(" %d/%d-%s ", hitCount, setSize, Serv.normDouble4(hitCoef));
			sb.append(bigHitReport).append("\t");
			
			// replace old data in hitReports by new isolated
			hitReports[i] = hitCount + 0.01 * setSize;
		}
		return sb.toString();
	}

	private int[] makeBigFrequencySet(double[] frequencyReport, int n) {
		int[] bigFrequencySet = new int[n];
		int lastPozytion = frequencyReport.length - 1;
		for (int i = lastPozytion, counter = 0; counter < n && i >= 0; i--, counter++) {
			bigFrequencySet[counter] = (int) (0.5 + 100.0 * (frequencyReport[i] - (int) frequencyReport[i]));
		}
		return bigFrequencySet;
	}

	private int[] makeSmallFrequencySet(double[] frequencyReport, int n) {
		List<Integer> smallFrequencySet = new ArrayList<>();
		for (int i = 0; i < frequencyReport.length; i++) {
			if (frequencyReport[i] > n + 1.0) {
				break;
			}
			int ball = (int) (0.5 + 100.0 * (frequencyReport[i] - (int) frequencyReport[i]));
			if (ball == 0) {
				continue;
			}
			smallFrequencySet.add(ball);
		}
		int[] result = new int[smallFrequencySet.size()];
		for (int i = 0; i < smallFrequencySet.size(); i++) {
			result[i] = smallFrequencySet.get(i);
		}
		return result;
	}

	private int countHits(int[] a, int[] b) {
		int hitCount = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				if (a[i] == b[j]) {
					++hitCount;
				}
			}
		}
		return hitCount;
	}
	
}
