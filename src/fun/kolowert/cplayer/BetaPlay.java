package fun.kolowert.cplayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReportPool;
import fun.kolowert.serv.Serv;
import fun.kolowert.serv.Timer;

/**
 * BetaPlay
 */
public class BetaPlay {
	
	private static GameType gameType = GameType.KENO;
	private static int playSet = 4;
	private static int histDeep = 80;
	private static int histShift = 1;
	private static int histShifts = 20;
	private static int[] matchingMask = new int[] { 100, 100, 100, 0, 0 };
	
	public static void main(String[] args) {
		Timer timer = new Timer();
		
		poolPlay();
		//multiPlay();

		System.out.println("\nFINISH ~ " + timer.reportExtended());
	}
	
	public static void poolPlay() {
		Timer timer = new Timer();
		
		System.out.println(
				"BetaPlay # poolPlay # " + System.currentTimeMillis() + " " + LocalDate.now() + " " + LocalTime.now());
		System.out.println("gameType:" + gameType.name() + " playSet:" + playSet + " histDeep:" + histDeep
				+ " matchingMask:" + Arrays.toString(matchingMask));
		System.out.println(Combinator.reportCombinationsQuantity(playSet, gameType.getGameSetSize()));

		PoolPlay poolPlay = new PoolPlay(gameType, playSet, matchingMask);
		MatchingReportPool pool = poolPlay.makeMatchingReportPool(histDeep, histShift, true);
		System.out.println(pool.size() + " lines in frequency reports");
		
		FreqReporterOnPoolSimple freqReporter = new FreqReporterOnPoolSimple(gameType, pool);
		double[] frequencyReport = freqReporter.getFrequencyReport();
		freqReporter.displayFrequencyReports(histShift);
		
		HitReporterSingle hitReporter = new HitReporterSingle();
		double[] hitReports = hitReporter.makeHitReports(gameType, histDeep, histShift, frequencyReport);
		String hitReportsReport = hitReporter.reportHitReports(hitReports);
		System.out.println("hitReport S " + histShift + ": " + hitReportsReport + "\n");
		
		System.out.println("poolPlay time - - -> " + timer.reportExtended() + "\n");
	}
	
	// ================================================================================================================
	public static void multiPlay() {
		System.out.println("multyPlay # BetaPlay " + System.currentTimeMillis());
		System.out.println("gameType:" + gameType.name() + " playSet:" + playSet + " histDeep:" + histDeep
				+ " matchingMask:" + Arrays.toString(matchingMask));
		System.out.println(Combinator.reportCombinationsQuantity(playSet, gameType.getGameSetSize()));

		int poolSizeSum = 0;
		double[] coefSum = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		int counter = 0;
		for (int indexHistShift = histShift + histShifts; indexHistShift >= histShift; indexHistShift--) {
			PoolPlay poolPlay = new PoolPlay(gameType, playSet, matchingMask);
			MatchingReportPool pool = poolPlay.makeMatchingReportPool(histDeep, indexHistShift, false);
			poolSizeSum += pool.size();
			
			FreqReporterOnPoolSimple freqReporter = new FreqReporterOnPoolSimple(gameType, pool);
			double[] frequencyReport = freqReporter.getFrequencyReport();
			//freqReporter.displayFrequencyReports(indexHistShift);
			
			HitReporterSingle hitReporter = new HitReporterSingle();
			double[] hitReports = hitReporter.makeHitReports(gameType, histDeep, indexHistShift, frequencyReport);

			String hitReportsReport = hitReporter.reportHitReports(hitReports);
			System.out.println("hitReport S " + Serv.normIntX(indexHistShift, 2, " ") + ":  " + hitReportsReport + "");

			for (int i = 0; i < coefSum.length; i++) {
				coefSum[i] += 1.0 * (int) hitReports[i] / (100.0 * (hitReports[i] - (int) hitReports[i]));
			}
			++counter;
		}
		
		if (counter != 0) {
			StringBuilder sb = new StringBuilder(" >>>>>  avgCoef:  ");
			for (int i = 0; i < coefSum.length; i++) {
				sb.append(Serv.normDoubleX(coefSum[i] / counter, 4)).append("  \t");
			}
			System.out.println("\n" + sb);
		}
		
		if (counter != 0) {
			int avgFreqReportLines = (int) (1.0 * poolSizeSum / counter); 
			System.out.println("\n" + avgFreqReportLines + "  avg lines in frequency reports");
		}
	}
	
}
