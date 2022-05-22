package fun.kolowert.cplayer;

import java.util.Arrays;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReportPool;
import fun.kolowert.serv.Serv;

/**
 * It should be created once for particular game type and Matching Reports Pool. It
 * counts appearance of balls in combinations from MatchingReport and makes
 * array of double where whole part is frequency and fractional part is ball.
 * 
 * @param GameType
 * @param matchingReportPool
 */
public class FreqReporterOnPoolSimple {
	
	private final GameType gameType;
	private final MatchingReportPool matchingReportPool;
	private double[] frequencyReport;
	private boolean isReportMade = false;

	public FreqReporterOnPoolSimple(GameType gameType, MatchingReportPool matchingReportPool) {
		this.gameType = gameType;
		this.matchingReportPool = matchingReportPool;
	}

	/**
	 * @return array of double where whole part is frequency and fractional part is
	 *         ball.
	 */
	public double[] getFrequencyReport() {
		if (!isReportMade) {
			frequencyReport = prepareFrequencyReport();
			isReportMade = true;
		}
		return frequencyReport;
	}

	private double[] prepareFrequencyReport() {
		// Count Frequency of balls in matchingReports
		int[] counter = new int[gameType.getGameSetSize() + 1];
		
		for (int[] playCombination : matchingReportPool.getPlayCombinations()) {
			for (int ball : playCombination) {
				++counter[ball];
			}
		}
		// prepare report
		double[] freqReport = new double[gameType.getGameSetSize() + 1];
		for (int i = 1; i < counter.length; i++) {
			freqReport[i] = counter[i] + 0.01 * i;
		}
		Arrays.sort(freqReport);

		return freqReport;
	}

	/**
	 * It prints to out: 1|'frequency'('ball'), 2|'frequency'('ball'), ...
	 * n|'frequency'('ball')
	 * 
	 * @param histShift - only for completing String-report
	 */
	public void displayFrequencyReports(int histShift) {
		double[] freqReport = getFrequencyReport();
		int c = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = freqReport.length - 1; i >= 0; i--) {
			String ball = Serv.normIntX((int) (0.5 + 100.0 * (freqReport[i] - (int) freqReport[i])), 2, "0");
			String frequency = Serv.normIntX((int) freqReport[i], 3, "0");
			sb.append(++c).append("|").append(frequency).append("(").append(ball).append(")").append("  ");
		}
		System.out.println("frqReport S " + histShift + " > " + sb.toString());
	}

	
}
