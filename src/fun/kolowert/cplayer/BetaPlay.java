package fun.kolowert.cplayer;

import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReport;
import fun.kolowert.serv.Serv;
import fun.kolowert.serv.Timer;

public class BetaPlay {

	public static void main(String[] args) {
		Timer timer = new Timer();

		multyPlay();

		System.out.println("\n~ ~ ~ FINISH ~ ~ ~");
		System.out.println(timer.reportExtended());
	}

	public static void multyPlay() {

		GameType gameType = GameType.KENO;
		int playSet = 4;
		int histDeep = 24;
		int histShiftFrom = 100; 
		int histShiftTo = 1;
		int[] matchingMask = new int[] { 100, 100, 0, 0, 0 };
		
		System.out.println("gameType:"+gameType.name() +" playSet:"+ playSet +" histDeep:"+ histDeep);
		
		double[] coefSum = new double[] { 0.0, 0.0 };
		int counter = 0;
		for (int indexHistShift = histShiftFrom; indexHistShift >= histShiftTo; indexHistShift--) {
			CPlay cPlay = new CPlay(gameType, playSet, histDeep, indexHistShift, matchingMask);
			List<MatchingReport> reports = cPlay.makePlayReports();
			double[] frequencyReport = cPlay.makeFrequencyReport(reports);
			// cPlay.displayFrequencyReports(frequencyReport);
			
			double[] hitReports = cPlay.makeHitReports(frequencyReport);
			System.out.println("hitReport " + indexHistShift + ": " + cPlay.reportHitReports(hitReports));
			
			coefSum[0] += 1.0 * (int) hitReports[0] / (100.0 * (hitReports[0] - (int) hitReports[0]));
			coefSum[1] += 1.0 * (int) hitReports[1] / (100.0 * (hitReports[1] - (int) hitReports[1]));
			++counter;
		}
		if (counter != 0) {
			System.out.println(" >> avgCoef: " + Serv.normDouble4(coefSum[0] / counter) 
					+ "\t" + Serv.normDouble4(coefSum[1] / counter));
		}
	}

	public static void demoPlay() {
		GameType gameType = GameType.KENO;
		int playSet = 4;
		int histDeep = 24;
		int histShift = 3;
		int[] matchingMask = new int[] { 100, 100, 0, 0, 0 };

		CPlay cPlay = new CPlay(gameType, playSet, histDeep, histShift, matchingMask);

		System.out.println(Combinator.reportCombinationsQuantity(playSet, gameType.getGameSetSize()));

		List<MatchingReport> reports = cPlay.makePlayReports();
		cPlay.displayPlayReports(reports);

		double[] frequencyReport = cPlay.makeFrequencyReport(reports);
		cPlay.displayFrequencyReports(frequencyReport);
		
		double[] hitReports = cPlay.makeHitReports(frequencyReport);
		System.out.println("hitReport " + histShift + ": " + cPlay.reportHitReports(hitReports));
	}

}
