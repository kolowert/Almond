package fun.kolowert.cplayer;

import java.time.LocalDate;
import java.time.LocalTime;
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
		//multyPlay();
		couplesPlay();

		System.out.println("\nFINISH ~ " + timer.reportExtended());
	}

	public static void multyPlay() {

		GameType gameType = GameType.KENO;
		int playSet = 4;
		int histDeep = 20;
		int histShiftFrom = 5;
		int histShiftTo = 2;
		int[] matchingMask = new int[] { 100, 100, 0, 0, 0 };

		System.out.println("multyPlay # BetaPlay " + System.currentTimeMillis());
		System.out.println("gameType:" + gameType.name() + " playSet:" + playSet + " histDeep:" + histDeep
				+ " matchingMask:" + Arrays.toString(matchingMask));
		System.out.println(Combinator.reportCombinationsQuantity(playSet, gameType.getGameSetSize()));

		double[] coefSum = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		int counter = 0;
		for (int indexHistShift = histShiftFrom; indexHistShift >= histShiftTo; indexHistShift--) {
			CPlay cPlay = new CPlay(gameType, playSet, histDeep, indexHistShift, matchingMask);
			List<MatchingReport> reports = cPlay.makePlayReports();
			double[] frequencyReport = new FreqReporterSingle(gameType, reports).getFrequencyReport();

			HitReporterSingle hitReporter = new HitReporterSingle();
			double[] hitReports = hitReporter.makeHitReports(gameType, histDeep, indexHistShift, frequencyReport);

			System.out.println("hitReportIso " + Serv.normInt2(indexHistShift) + ": "
					+ hitReporter.reportIsolatedHitReports(hitReports));

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
		int histDeep = 52;
		int histShift = 5;
		int[] matchingMask = new int[] { 0, 0, 0, 0, 0 };
		int betSize = 3;

		System.out.println(
				"demoPlay # BetaPlay " + System.currentTimeMillis() + " " + LocalDate.now() + " " + LocalTime.now());
		System.out.println("gameType:" + gameType.name() + " playSet:" + playSet + " histDeep:" + histDeep
				+ " matchingMask:" + Arrays.toString(matchingMask));
		System.out.println(Combinator.reportCombinationsQuantity(playSet, gameType.getGameSetSize()));

		CPlay cPlay = new CPlay(gameType, playSet, histDeep, histShift, matchingMask);

		List<MatchingReport> reports = cPlay.makePlayReports();
		// cPlay.displayPlayReports(reports);
		System.out.println(reports.size() + " lines in frequency reports");

		FreqReporterSingle freqReporter = new FreqReporterSingle(gameType, reports);
		double[] frequencyReport = freqReporter.getFrequencyReport();
		freqReporter.displayFrequencyReports(histShift);

		CoupleHitReporter coupleReporter = new CoupleHitReporter(gameType, histShift, frequencyReport);
		coupleReporter.displayReport();

		HitReporterSingle hitReporter = new HitReporterSingle();
		double[] hitReports = hitReporter.makeHitReports(gameType, histDeep, histShift, frequencyReport);
		String isolatedHitReportsReport = hitReporter.reportIsolatedHitReports(hitReports);
		System.out.println("hitReport S " + histShift + ": " + isolatedHitReportsReport + "\n");

		FreqReporterMulti freqReporterMultic = new FreqReporterMulti(gameType, betSize - 1, reports);
		freqReporterMultic.displayFrequencyReports(histShift);

		FreqReporterMulti freqReporterMulti = new FreqReporterMulti(gameType, betSize, reports);
		freqReporterMulti.displayFrequencyReports(histShift);

//		FreqReporterMulti freqReporterMultis = new FreqReporterMulti(gameType, betSize + 1, reports);
//		freqReporterMultis.displayFrequencyReports(histShift);

	}

	public static void couplesPlay() {
		GameType gameType = GameType.KENO;
		int playSet = 4;
		int histDeep = 24;
		int histShift = 1;
		int multiShift = 5;
		int[] matchingMask = new int[] { 100, 100, 0, 0, 0 };

		System.out.println(
				"Couples Play # BetaPlay " + System.currentTimeMillis() + " " + LocalDate.now() + " " + LocalTime.now());
		System.out.println("gameType:" + gameType.name() + " playSet:" + playSet + " histDeep:" + histDeep
				+ " matchingMask:" + Arrays.toString(matchingMask));
		System.out.println(Combinator.reportCombinationsQuantity(playSet, gameType.getGameSetSize()));

		for (int shift = histShift + multiShift - 1; shift >= histShift; shift--) {

			CPlay cPlay = new CPlay(gameType, playSet, histDeep, shift, matchingMask);

			List<MatchingReport> reports = cPlay.makePlayReports();
			// cPlay.displayPlayReports(reports);
			// System.out.println(reports.size() + " lines in frequency reports");

			FreqReporterSingle freqReporter = new FreqReporterSingle(gameType, reports);
			double[] frequencyReport = freqReporter.getFrequencyReport();
			// freqReporter.displayFrequencyReports(shift);

			CoupleHitReporter coupleReporter = new CoupleHitReporter(gameType, shift, frequencyReport);
			coupleReporter.displayReport();
		}
	}

}
