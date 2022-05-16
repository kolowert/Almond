package fun.kolowert.cplayer;

import java.util.Arrays;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReport;
import fun.kolowert.serv.Serv;

/**
 * It should be created once for particular game type and matching reports. It
 * counts appearance of balls in combinations from MatchingReport and makes
 * array of double where whole part is frequency and fractional part is ball.
 * 
 * @param GameType
 * @param matchingReports
 */
public class FreqReporter {

	private final GameType gameType;
	private final List<MatchingReport> matchingReports;
	private final double[] frequencyReport;

	public FreqReporter(GameType gameType, List<MatchingReport> matchingReports) {
		this.gameType = gameType;
		this.matchingReports = matchingReports;
		frequencyReport = prepareFrequencyReport();
	}

	private double[] prepareFrequencyReport() {
		// Count Frequency of balls in matchingReports
		int[] counter = new int[gameType.getGameSetSize() + 1];
		for (MatchingReport report : matchingReports) {
			for (int ball : report.getPlayCombination()) {
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
	 * @return array of double where whole part is frequency and fractional part is
	 *         ball.
	 */
	public double[] makeFrequencyReport() {
		return frequencyReport;
	}

	/**
	 * It prints to out: 1|'frequency'('ball'), 2|'frequency'('ball'), ...
	 * n|'frequency'('ball')
	 * 
	 * @param histShift - only for completing String-report
	 */
	public void displayFrequencyReports(int histShift) {
		int c = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = frequencyReport.length - 1; i >= 0; i--) {
			String ball = Serv.normInt2((int) (0.5 + 100.0 * (frequencyReport[i] - (int) frequencyReport[i])));
			String frequency = Serv.normInt3((int) frequencyReport[i]);
			sb.append(++c).append("|").append(frequency).append("(").append(ball).append(")").append("  ");
		}
		System.out.println("frqReport " + histShift + " > " + sb.toString());
	}

}
