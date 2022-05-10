package fun.kolowert.cplayer;

import java.util.Arrays;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReport;
import fun.kolowert.serv.Serv;
import fun.kolowert.serv.Timer;
/**
 * BetaPlay
 */
public class BetaPlay {

	public static void main(String[] args) {
		Timer timer = new Timer();
		
		//demoPlay();
		multyPlay();

		System.out.println("\n~ ~ ~ FINISH ~ ~ ~");
		System.out.println(timer.reportExtended());
	}

	public static void multyPlay() {

		GameType gameType = GameType.SUPER;
		int playSet = 6;
		int histDeep = 62;
		int histShiftFrom = 17; 
		int histShiftTo = 15;
		int[] matchingMask = new int[] { 0, 0, 0, 0, 0 };
		
		System.out.println("multyPlay # BetaPlay " + System.currentTimeMillis());
		System.out.println("gameType:"+gameType.name() +" playSet:"+ playSet +" histDeep:"+ histDeep 
				+ " matchingMask:" + Arrays.toString(matchingMask));
		System.out.println(Combinator.reportCombinationsQuantity(playSet, gameType.getGameSetSize()));
		
		double[] coefSum = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		int counter = 0;
		for (int indexHistShift = histShiftFrom; indexHistShift >= histShiftTo; indexHistShift--) {
			CPlay cPlay = new CPlay(gameType, playSet, histDeep, indexHistShift, matchingMask);
			List<MatchingReport> reports = cPlay.makePlayReports();
			double[] frequencyReport = cPlay.makeFrequencyReport(reports);
			
			double[] hitReports = cPlay.makeHitReports(frequencyReport);
			
			System.out.println("hitReportIso " + Serv.normInt2(indexHistShift) 
					+ ": " + cPlay.reportIsolatedHitReports(hitReports));
			
			for (int i = 0; i < coefSum.length; i++) {
				coefSum[i] += 1.0 * (int) hitReports[i] / (100.0 * (hitReports[i] - (int) hitReports[i]));
			}
			++counter;
		}
		
		if (counter != 0) {
			StringBuilder sb = new StringBuilder(" >>>>>  avgCoef:  ");
			for (int i = 0; i < coefSum.length; i++) {
				sb.append(Serv.normDouble4(coefSum[i] / counter)).append("   \t");
			}
			System.out.println("\n" + sb);
		}
	}

	public static void demoPlay() {
		GameType gameType = GameType.SUPER;
		int playSet = 6;
		int histDeep = 62;
		int histShift = 10;
		int[] matchingMask = new int[] { 0, 0, 0, 0, 0 };

		CPlay cPlay = new CPlay(gameType, playSet, histDeep, histShift, matchingMask);
		
		System.out.println("demoPlay # BetaPlay " + System.currentTimeMillis());
		System.out.println("gameType:"+gameType.name() +" playSet:"+ playSet +" histDeep:"+ histDeep 
				+ " matchingMask:" + Arrays.toString(matchingMask));
		System.out.println(Combinator.reportCombinationsQuantity(playSet, gameType.getGameSetSize()));

		List<MatchingReport> reports = cPlay.makePlayReports();
		cPlay.displayPlayReports(reports);

		double[] frequencyReport = cPlay.makeFrequencyReport(reports);
		cPlay.displayFrequencyReports(frequencyReport);
		
		double[] hitReports = cPlay.makeHitReports(frequencyReport);
		System.out.println("hitReport " + histShift + ": " + cPlay.reportHitReports(hitReports));
	}

}
