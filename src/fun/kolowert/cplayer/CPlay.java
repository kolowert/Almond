package fun.kolowert.cplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.HistAnalizer;
import fun.kolowert.common.MatchingReport;
import fun.kolowert.serv.Timer;

public class CPlay {
	
	private static final GameType GAME_TYPE = GameType.KENO;
	
	private static final int COMB_SIZE = 3;
	private static final int HIST_DEEP = 30;
	private static final int HIST_SHIFT = 3;

	private int combSetSize;
	private int gameSetSize;

	public CPlay() {
		if (GAME_TYPE == GameType.KENO && GAME_TYPE.getCombSetSize() > COMB_SIZE) {
			combSetSize = COMB_SIZE;
		} else {
			combSetSize = GAME_TYPE.getCombSetSize();
		}
		gameSetSize = GAME_TYPE.getGameSetSize();
	}

	public static void main(String[] args) {
		CPlay cPlay = new CPlay();
		Timer timer = new Timer();

		cPlay.playAllCombinations(true);

		System.out.println("\n~~~ FINISH ~~~");
		System.out.println(timer.reportExtended());
	}

	public void playAllCombinations(boolean doit) {
		if (!doit)
			return;

		System.out.println("~~~ playAllCombinations ~~~");
		Combinator.calculateCombinations(combSetSize, gameSetSize);

		List<MatchingReport> reports = new ArrayList<>();
		HistAnalizer histAnalyzer = new HistAnalizer(GAME_TYPE, HIST_DEEP, HIST_SHIFT);
		Combinator combinator = new Combinator(combSetSize, gameSetSize);
		
		while (!combinator.isFinished()) {
			int[] playCombination = combinator.makeNext();
			MatchingReport analizReport = histAnalyzer.makeMatchingReport(playCombination, "-");
			int[] matching = analizReport.getMatching();
			if (
					   matching[matching.length - 1] <= 0
					&& matching[matching.length - 2] <= 0
					&& matching[matching.length - 3] <= 15
					&& matching[matching.length - 4] <= 30
					// && matching[matching.length - 5] <= 100
			   ) 
			{
				reports.add(analizReport);
			}
		}

		// display
		int counter = 0;
		for (MatchingReport report : reports) {
			String pn = ++counter < 10 ? "00" + counter : counter < 100 ? "0" + counter : "" + counter;
			String labe = "  " + combSetSize + "/" + gameSetSize;
			System.out.println(pn + labe + "\t" + report.report());
		}
		
		String frequencyReport = makeFrequencyReport(reports);
		System.out.println("Frequency of balls in matchingReports: frequency.ball");
		System.out.println(frequencyReport);
	}

	private String makeFrequencyReport(List<MatchingReport> matchingReports) {
		// Count Frequency of balls in matchingReports
		int[] counter = new int[GAME_TYPE.getGameSetSize() + 1];
		for (MatchingReport report : matchingReports) {
			for (int ball : report.getPlayCombination()) {
				++counter[ball];
			}
		}
		// prepare report
		double[] freqReport = new double[GAME_TYPE.getGameSetSize() + 1];
		for (int i = 1; i < counter.length; i++) {
			freqReport[i] = counter[i] + 0.01 * i;
		}
		Arrays.sort(freqReport);
		
		StringBuilder sb = new StringBuilder();
		for (int i = freqReport.length - 1; i >= 0; i--) {
			if (freqReport[i] > 1.0) {
				sb.append(freqReport[i]).append(" ");
			}
		}
		return sb.toString();
	}

}
