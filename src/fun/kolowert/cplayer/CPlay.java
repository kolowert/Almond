package fun.kolowert.cplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.HistAnalizer;
import fun.kolowert.common.MatchingReport;
import fun.kolowert.serv.Timer;

public class CPlay {
	
	private GameType gameType;
	private int histDeep;
	private int histShift;

	private int combSetSize;
	private int gameSetSize;
	
	private int[] matchingMask;

	public CPlay(GameType gameType, int combSize, int histDeep, int histShift, int[] matchingMask) {
		if (gameType == GameType.KENO && gameType.getCombSetSize() > combSize) {
			combSetSize = combSize;
		} else {
			combSetSize = gameType.getCombSetSize();
		}
		
		this.gameType = gameType;
		this.histDeep = histDeep;
		this.histShift = histShift;
		this.gameSetSize = gameType.getGameSetSize();
		this.matchingMask = matchingMask;
		
	}
	
	// debugging
	public static void main(String[] args) {
		CPlay cPlay = new CPlay(GameType.KENO, 4, 30, 3, new int[] { 100, 30, 1, 0, 0 });
		Timer timer = new Timer();
		
		List<MatchingReport> reports = cPlay.makePlayReports();
		cPlay.displayPlayReports(reports);
		
		double[] frequencyReport = cPlay.makeFrequencyReport(reports);
		cPlay.displayFrequencyReports(frequencyReport);
		
		System.out.println("\n~~~ FINISH ~~~");
		System.out.println(timer.reportExtended());
	}
	
	public List<MatchingReport> makePlayReports() {
		List<MatchingReport> reports = new ArrayList<>();
		HistAnalizer histAnalyzer = new HistAnalizer(gameType, histDeep, histShift);
		Combinator combinator = new Combinator(combSetSize, gameSetSize);
		
		while (!combinator.isFinished()) {
			int[] playCombination = combinator.makeNext();
			MatchingReport analizReport = histAnalyzer.makeMatchingReport(playCombination, "-");
			int[] matching = analizReport.getMatching();
			if (
					   matching[matching.length - 1] <= matchingMask[4]
					&& matching[matching.length - 2] <= matchingMask[3]
					&& matching[matching.length - 3] <= matchingMask[2]
					&& matching[matching.length - 4] <= matchingMask[1]
					&& matching[matching.length - 5] <= matchingMask[0]
			   ) 
			{
				reports.add(analizReport);
			}
		}
		return reports;
	}
	
	private void displayPlayReports(List<MatchingReport> reports) {
		int counter = 0;
		for (MatchingReport report : reports) {
			String pn = ++counter < 10 ? "00" + counter : counter < 100 ? "0" + counter : "" + counter;
			String labe = "  " + combSetSize + "/" + gameSetSize;
			System.out.println(pn + labe + "\t" + report.report());
		}
	}

	public double[] makeFrequencyReport(List<MatchingReport> matchingReports) {
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
	
	private void displayFrequencyReports(double[] freqReport) {
		int c = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = freqReport.length - 1; i >= 0; i--) {
			if (freqReport[i] > 1.0) {
				sb.append(++c).append("|").append(freqReport[i]).append("  ");
			}
		}
		System.out.println("Frequency of balls in matchingReports: frequency.ball");
		System.out.println(sb.toString());
	}

}
