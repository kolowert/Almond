package fun.kolowert.cplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReport;

/**
 * It is the same as FreqReporterSingle but for couples It counts appearance of
 * multi-ball-bet in combinations from MatchingReport
 */
public class FreqReporterMulti {
	private final GameType gameType;
	private final int betSize;
	private final List<MatchingReport> matchingReports;
	private FreqReportMultiBet[] frequencyReport;
	private boolean isReportMade = false;

	public FreqReporterMulti(GameType gameType, int betSize, List<MatchingReport> matchingReports) {
		this.gameType = gameType;
		this.betSize = betSize;
		this.matchingReports = matchingReports;
	}
	
	public FreqReportMultiBet[] getFrequencyReport() {
		if (!isReportMade) {
			frequencyReport = makeFrequencyReport();
		}
		return frequencyReport;
	}
	
	private FreqReportMultiBet[] makeFrequencyReport() {
		int rSize = (int) Combinator.calculateCombinations(betSize, gameType.getGameSetSize());
		List<FreqReportMultiBet> rawReport = new ArrayList<>(rSize + 1);
		Combinator combinator = new Combinator(betSize, gameType.getGameSetSize());
		
		// All bets cycle ---
		while (combinator.hasNext()) {
			int[] bet = combinator.makeNext();
			FreqReportMultiBet coupleReport = new FreqReportMultiBet(bet);
			
			// MatchingReports cycle ---
			for (MatchingReport matchingReport : matchingReports) {
				int[] playCombination = matchingReport.getPlayCombination();
				if (isCoupleMatchingCombination(bet, playCombination)) {
					coupleReport.increaseFrequency();
				}
			}
			rawReport.add(coupleReport);
		}
		FreqReportMultiBet[] result = rawReport.toArray(new FreqReportMultiBet[0]);
		Arrays.sort(result);
		isReportMade = true;
		return result;
	}
	
	private boolean isCoupleMatchingCombination(int[] bet, int[] comb) {
		int counter = 0;
		for (int c : bet) {
			for (int k : comb) {
				if (c == k) {
					++counter;
					break;
				}
			}
		}		
		return counter == betSize;
	}

	/**
	 * It prints to out: 1|'frequency'('aBall-bBall-..nBall'), 2|'frequency'('aBall-bBall-..nBall'), .. n|'frequency'('aBall-bBall-..nBall')
	 * @param histShift - only for completing String-report
	 */
	public void displayFrequencyReports(int histShift, FreqReportMultiBet[] freqReport) {
		int c = 0;
		StringBuilder sb = new StringBuilder();
		int reportLength = 10;
		for (int i = freqReport.length - 1; i >= 0; i--) {
			sb	.append(++c)
				.append("|")
				.append(freqReport[i].report())
				.append("  ");
			if (--reportLength <= 0) { break; }
		}
		System.out.println("frqReprt M" + betSize + " " + histShift + " > " + sb.toString());
	}
	
	
	public void displayFrequencyReports(int histShift) {
		displayFrequencyReports(histShift, getFrequencyReport());
	}
}
