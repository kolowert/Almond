package fun.kolowert.cplayer;

import java.util.ArrayList;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.HistAnalizer;
import fun.kolowert.common.MatchingReport;
import fun.kolowert.serv.Timer;

public class CPlay {
	
	private final GameType gameType;
	private final int combSetSize;
	private final int gameSetSize;

	private final int[] matchingMask;

	private final HistAnalizer histAnalyzer;

	public CPlay(GameType gameType, int combSize, int histDeep, int histShift, int[] matchingMask) {
		if (gameType == GameType.KENO && gameType.getCombSetSize() > combSize) {
			combSetSize = combSize;
		} else {
			combSetSize = gameType.getCombSetSize();
		}
		
		this.gameType = gameType;
		this.gameSetSize = gameType.getGameSetSize();
		this.matchingMask = matchingMask;

		histAnalyzer = new HistAnalizer(gameType, histDeep, histShift);
	}

	// debugging
	public static void main(String[] args) {
		CPlay cPlay = new CPlay(GameType.KENO, 4, 24, 3, new int[] { 100, 100, 0, 0, 0 });
		Timer timer = new Timer();

		List<MatchingReport> reports = cPlay.makePlayReports();
		cPlay.displayPlayReports(reports);

		System.out.println("\n~~~ FINISH ~~~");
		System.out.println(timer.reportExtended());
	}

	public List<MatchingReport> makePlayReports() {
		List<MatchingReport> reports = new ArrayList<>();
		Combinator combinator = new Combinator(combSetSize, gameSetSize);

		while (!combinator.isFinished()) {
			int[] playCombination = combinator.makeNext();
			MatchingReport matchingReport = histAnalyzer.makeMatchingReport(playCombination, "-");
			int[] matching = matchingReport.getMatching();
			if (       matching[matching.length - 1] <= matchingMask[4] 
					&& matching[matching.length - 2] <= matchingMask[3]
					&& matching[matching.length - 3] <= matchingMask[2]
					&& matching[matching.length - 4] <= matchingMask[1]
					&& matching[matching.length - 5] <= matchingMask[0]
				) 
			{
				matchingReport.makeScore(gameType);
				reports.add(matchingReport);
			}
		}
		return reports;
	}

	public void displayPlayReports(List<MatchingReport> reports) {
		int counter = 0;
		for (MatchingReport report : reports) {
			String pn = ++counter < 10 ? "00" + counter : counter < 100 ? "0" + counter : "" + counter;
			String labe = "  " + combSetSize + "/" + gameSetSize;
			System.out.println(pn + labe + "\t" + report.report());
		}
	}

}
