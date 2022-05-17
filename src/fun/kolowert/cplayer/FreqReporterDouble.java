package fun.kolowert.cplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReport;
import fun.kolowert.serv.Serv;

/**
 * It is the same as FreqReporterSingle but for couples It counts appearance of
 * ball-couples in combinations from MatchingReport
 */
public class FreqReporterDouble {
	private final GameType gameType;
	private final List<MatchingReport> matchingReports;
	private final Map<int[], FreqReportForCouple> frequencyReport;

	public FreqReporterDouble(GameType gameType, List<MatchingReport> matchingReports) {
		this.gameType = gameType;
		this.matchingReports = matchingReports;
		frequencyReport = prepareFrequencyReport();
	}

	private Map<int[], FreqReportForCouple> prepareFrequencyReport() {

		Map<int[], FreqReportForCouple> map = new HashMap<>();
		
		Combinator combinator = new Combinator(2, gameType.getGameSetSize());
		
		// All couple cycle ---
		while (combinator.hasNext()) {
			int[] couple = combinator.makeNext();
			
			// MatchingReports cycle ---
			for (MatchingReport matchingReport : matchingReports) {
				
				int[] playCombination = matchingReport.getPlayCombination();
				
				if (isCoupleMatchingCombination(2, couple, playCombination)) {
					
					if (map.containsKey(couple)) {
						FreqReportForCouple coupleReport = map.get(couple);
						coupleReport.increaseFrequency();
						map.replace(couple, coupleReport);
					} else {
						FreqReportForCouple coupleReport = new FreqReportForCouple(couple);
						coupleReport.increaseFrequency();
						map.put(couple, coupleReport);
					}	
				}
			}
		}
		return map;
	}
	
	private boolean isCoupleMatchingCombination(int multiplier, int[] couple, int[] comb) {
		int counter = 0;
		for (int c : couple) {
			for (int k : comb) {
				if (c == k) {
					++counter;
					break;
				}
			}
		}		
		return counter == multiplier;
	}
	
	public FreqReportForCouple[] makeFrequencyReport() {
		Collection<FreqReportForCouple> collection = frequencyReport.values();
		List<FreqReportForCouple> list = new ArrayList<>(collection);
		FreqReportForCouple[] result = list.toArray(new FreqReportForCouple[0]);
		Arrays.sort(result);
		return result;
	}

	/**
	 * It prints to out: 1|'frequency'('aBall-bBall'), 2|'frequency'('aBall-bBall'), .. n|'frequency'('aBall-bBall')
	 * 
	 * @param histShift - only for completing String-report
	 */
	public void displayFrequencyReports(int histShift, FreqReportForCouple[] freqReport2) {
		int c = 0;
		StringBuilder sb = new StringBuilder();
		
		for (int i = freqReport2.length - 1; i >= 0; i--) {
			int[] couple = freqReport2[i].getBallCouple();
			sb	.append(++c)
				.append("|")
				.append(Serv.normInt3(freqReport2[i].getFrequency()))
				.append("(")
				.append(Serv.normInt2(couple[0]))
				.append(" ")
				.append(Serv.normInt2(couple[1]))
				.append(")")
				.append("  ");
		}
		System.out.println("frqReport D " + histShift + " > " + sb.toString());
	}
	
	
	public void displayFrequencyReports(int histShift) {
		displayFrequencyReports(histShift, makeFrequencyReport());
	}
}
