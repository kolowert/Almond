package fun.kolowert.cplayer;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReporter;
import fun.kolowert.serv.Serv;

public class HitReporterSingle {

	public double[] makeHitReports(
			GameType gameType, int histDeep, int histShift, double[] frequencyReport, int[] hitMask) {
		
		double[] result = new double[hitMask.length];

		MatchingReporter matchingReporter = new MatchingReporter(gameType, histDeep, histShift);
		int[] nextLineOfHistBlock = matchingReporter.getHistHandler().getNextLineOfHistBlock(histShift);

		for (int ind = 0; ind < hitMask.length; ind++) {
			int[] bigFrequencySet = makeBigFrequencySet(frequencyReport, hitMask[ind]);
			int bigHitCount = countHits(bigFrequencySet, nextLineOfHistBlock);
			result[ind] = bigHitCount + 0.01 * bigFrequencySet.length;
		}

		return result;
	}

	public String reportHitReports(double[] hitReports) {
		StringBuilder sb = new StringBuilder();
		for (double r : hitReports) {
			int hitCount = (int) r;
			int setSize = (int) (0.5 + 100 * (r - (int) r));
			double hitCoef = 1.0 * hitCount / setSize;
			String bigHitReport = String.format(" %d/%d-%s ", hitCount, setSize, Serv.normDoubleX(hitCoef, 4));
			sb.append(bigHitReport).append("   ");
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
			String bigHitReport = String.format(" %d/%d-%s ", hitCount, setSize, Serv.normDoubleX(hitCoef, 4));
			sb.append(bigHitReport).append("   ");

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
