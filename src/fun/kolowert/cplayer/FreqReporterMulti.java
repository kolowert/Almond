package fun.kolowert.cplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private final Map<int[], FreqReportMultiBet> frequencyReport;

	public FreqReporterMulti(GameType gameType, int betSize, List<MatchingReport> matchingReports) {
		this.gameType = gameType;
		this.betSize = betSize;
		this.matchingReports = matchingReports;
		frequencyReport = prepareFrequencyReport();
	}

	private Map<int[], FreqReportMultiBet> prepareFrequencyReport() {

		Map<int[], FreqReportMultiBet> map = new HashMap<>();
		
		Combinator combinator = new Combinator(betSize, gameType.getGameSetSize());
		
		// All couple cycle ---
		while (combinator.hasNext()) {
			int[] couple = combinator.makeNext();
			
			// MatchingReports cycle ---
			for (MatchingReport matchingReport : matchingReports) {
				
				int[] playCombination = matchingReport.getPlayCombination();
				
				if (isCoupleMatchingCombination(couple, playCombination)) {
					
					if (map.containsKey(couple)) {
						FreqReportMultiBet coupleReport = map.get(couple);
						coupleReport.increaseFrequency();
						map.replace(couple, coupleReport);
					} else {
						FreqReportMultiBet coupleReport = new FreqReportMultiBet(couple);
						coupleReport.increaseFrequency();
						map.put(couple, coupleReport);
					}	
				}
			}
		}
		return map;
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
	
	public FreqReportMultiBet[] makeFrequencyReport() {
		Collection<FreqReportMultiBet> collection = frequencyReport.values();
		List<FreqReportMultiBet> list = new ArrayList<>(collection);
		FreqReportMultiBet[] result = list.toArray(new FreqReportMultiBet[0]);
		Arrays.sort(result);
		return result;
	}

	/**
	 * It prints to out: 1|'frequency'('aBall-bBall-..nBall'), 2|'frequency'('aBall-bBall-..nBall'), .. n|'frequency'('aBall-bBall-..nBall')
	 * @param histShift - only for completing String-report
	 */
	public void displayFrequencyReports(int histShift, FreqReportMultiBet[] freqReport2) {
		int c = 0;
		StringBuilder sb = new StringBuilder();
		int reportLength = 10;
		for (int i = freqReport2.length - 1; i >= 0; i--) {
			sb	.append(++c)
				.append("|")
				.append(freqReport2[i].report())
				.append("  ");
			if (--reportLength <= 0) { break; }
		}
		System.out.println("frqReport M " + histShift + " > " + sb.toString());
	}
	
	
	public void displayFrequencyReports(int histShift) {
		displayFrequencyReports(histShift, makeFrequencyReport());
	}
}
