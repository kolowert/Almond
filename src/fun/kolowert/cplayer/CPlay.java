package fun.kolowert.cplayer;

import java.util.ArrayList;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReporter;
import fun.kolowert.common.MatchingReport;
import fun.kolowert.serv.Serv;
import fun.kolowert.serv.Timer;

public class CPlay {
	
	private final int combSetSize;
	private final int gameSetSize;

	private final int[] matchingMask;

	private final MatchingReporter matchingReporter;

	public CPlay(GameType gameType, int combSize, int histDeep, int histShift, int[] matchingMask) {
		if (gameType == GameType.KENO && gameType.getCombSetSize() > combSize) {
			combSetSize = combSize;
		} else {
			combSetSize = gameType.getCombSetSize();
		}
		
		this.gameSetSize = gameType.getGameSetSize();
		this.matchingMask = matchingMask;

		matchingReporter = new MatchingReporter(gameType, histDeep, histShift);
	}

	public List<MatchingReport> makePlayReports() {
		List<MatchingReport> reports = new ArrayList<>();
		Combinator combinator = new Combinator(combSetSize, gameSetSize);

		while (!combinator.isFinished()) {
			int[] playCombination = combinator.makeNext();
			MatchingReport matchingReport = matchingReporter.makeMatchingReport(playCombination, "-");
			int[] matching = matchingReport.getMatching();
			if (       matching[matching.length - 1] <= matchingMask[4] 
					&& matching[matching.length - 2] <= matchingMask[3]
					&& matching[matching.length - 3] <= matchingMask[2]
					&& matching[matching.length - 4] <= matchingMask[1]
					&& matching[matching.length - 5] <= matchingMask[0]
				) 
			{
				reports.add(matchingReport);
			}
		}
		return reports;
	}

	public void displayPlayReports(List<MatchingReport> reports) {
		int counter = 0;
		for (MatchingReport report : reports) {
			String labe = "  " + combSetSize + "/" + gameSetSize;
			System.out.println(Serv.normIntX(++counter, 3, " ") + labe + "\t" + report.report());
		}
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
	
}
