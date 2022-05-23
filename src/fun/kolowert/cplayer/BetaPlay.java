package fun.kolowert.cplayer;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReportPool;
import fun.kolowert.serv.Serv;
import fun.kolowert.serv.Timer;

/**
 * BetaPlay
 */
public class BetaPlay {

	private static final GameType gameType = GameType.MAXI;
	private static final int playSet = 5;
	private static final int histDeep = 45;
	private static final int histShift = 1;
	private static final int histShifts = 3;
	private static final int[] matchingMask = new int[] { 100, 100, 0, 0, 0, 0 };

	private static final int[] hitMaskPlain = { 5, 10, 15, 20, 25, 30, 35, 40, 45 };
	private static final int[] hitMaskIsolated = { 5, 10, 15, 20, 25, 30, 35, 40, 45 };

	public static void main(String[] args) {
		Timer timer = new Timer();

		boolean display = false;
		poolPlay(display);

		boolean plainHits = false;
		boolean isolatedHits = false;
		boolean pureFreqReport = true;
		multiPlay(plainHits, isolatedHits, pureFreqReport);

		System.out.println("\nFINISH ~ " + timer.reportExtended());
	}

	public static void poolPlay(boolean display) {
		Timer timer = new Timer();

		System.out.println("~ Single ~ " + gameType.name() + " ~ playSet:" + playSet + " histDeep:" + histDeep
				+ " matchingMask:" + Arrays.toString(matchingMask) + " histLine:" + histShift + "   "
				+ LocalTime.now().toString().substring(0, 8));
		System.out.println(Combinator.reportCombinationsQuantity(playSet, gameType.getGameSetSize()));

		PoolPlay poolPlay = new PoolPlay(gameType, playSet, matchingMask);
		MatchingReportPool pool = poolPlay.makeMatchingReportPool(histDeep, histShift, display);
		System.out.println(pool.size() + " lines in frequency reports");

		FreqReporterOnPoolSimple freqReporter = new FreqReporterOnPoolSimple(gameType, pool);
		double[] frequencyReport = freqReporter.getFrequencyReport();
		freqReporter.displayFrequencyReports(histShift);

		HitReporterSingle hitReporter = new HitReporterSingle();
		double[] hitReports = hitReporter.makeHitReports(gameType, histDeep, histShift, frequencyReport,
				hitMaskPlain);
		String hitReportsReport = hitReporter.reportHitReports(hitReports);
		System.out.println("hitReport S " + histShift + ": " + hitReportsReport + "\n");

		System.out.println("poolPlay time - - -> " + timer.reportExtended() + "\n");
	}
	// ----------------------------------------------------------------------------------------------------------------

	public static void multiPlay(boolean plainHits, boolean isolatedHits, boolean pureFreqReport) {
		System.out.println("# Multy # " + gameType.name() + " # playSet:" + playSet + " histDeep:" + histDeep
				+ " matchingMask:" + Arrays.toString(matchingMask) + " histLines:" + histShift + "/" + histShifts
				+ "   " + LocalTime.now().toString().substring(0, 8));
		System.out.println(Combinator.reportCombinationsQuantity(playSet, gameType.getGameSetSize()));

		List<String> pureFreqReports = new ArrayList<>();
		HitReporterSingle hitReporter = new HitReporterSingle();
		
		int poolSizeSum = 0;
		double[] plainHitAvgCoefSum = new double[hitMaskPlain.length];
		double[] isolatedHitAvgCoefSum = new double[hitMaskIsolated.length];
		int counter = 0;
		int plainHitsCounter = 0;
		int isolatedHitsCounter = 0;
		for (int indexHistShift = histShift + histShifts - 1; indexHistShift >= histShift; indexHistShift--) {
			PoolPlay poolPlay = new PoolPlay(gameType, playSet, matchingMask);
			MatchingReportPool pool = poolPlay.makeMatchingReportPool(histDeep, indexHistShift, false);
			poolSizeSum += pool.size();

			FreqReporterOnPoolSimple freqReporter = new FreqReporterOnPoolSimple(gameType, pool);
			double[] frequencyReport = freqReporter.getFrequencyReport();
			
			if (pureFreqReport) {
				pureFreqReports.add(freqReporter.reportPureFrequencyReports(indexHistShift));
			}

			if (plainHits) {
				double[] hitReports = hitReporter.makeHitReports(gameType, histDeep, indexHistShift, frequencyReport,
						hitMaskPlain);
				String hitReportsReport = hitReporter.reportHitReports(hitReports);
				System.out.println(
						"hitReport plain " + Serv.normIntX(indexHistShift, 2, " ") + ":  " + hitReportsReport + "");

				for (int i = 0; i < plainHitAvgCoefSum.length; i++) {
					plainHitAvgCoefSum[i] += 1.0 * (int) hitReports[i]
							/ (100.0 * (hitReports[i] - (int) hitReports[i]));
				}
				++plainHitsCounter;
			}

			if (isolatedHits) {
				double[] hitReports = hitReporter.makeHitReports(gameType, histDeep, indexHistShift, frequencyReport,
						hitMaskIsolated);
				String hitReportsReport = hitReporter.reportIsolatedHitReports(hitReports);
				System.out.println(
						"hitReport isltd " + Serv.normIntX(indexHistShift, 2, " ") + ":  " + hitReportsReport + "");

				for (int i = 0; i < isolatedHitAvgCoefSum.length; i++) {
					isolatedHitAvgCoefSum[i] += 1.0 * (int) hitReports[i]
							/ (100.0 * (hitReports[i] - (int) hitReports[i]));
				}
				++isolatedHitsCounter;
			}
			++counter;
		}

		if (plainHits) {
			if (plainHitsCounter != 0) {
				StringBuilder sb = new StringBuilder("PlainHit    avgCoef >>  ");
				for (int i = 0; i < plainHitAvgCoefSum.length; i++) {
					sb.append(Serv.normDoubleX(plainHitAvgCoefSum[i] / plainHitsCounter, 4)).append("         ");
				}
				System.out.println("\n" + sb);
			}
		}

		if (isolatedHits) {
			if (isolatedHitsCounter != 0) {
				StringBuilder sb = new StringBuilder("IsolatedHit avgCoef >>  ");
				for (int i = 0; i < isolatedHitAvgCoefSum.length; i++) {
					sb.append(Serv.normDoubleX(isolatedHitAvgCoefSum[i] / isolatedHitsCounter, 4))
							.append("         ");
				}
				System.out.println("\n" + sb);
			}
		}
		
		if (counter != 0) {
			int avgFreqReportLines = (int) (1.0 * poolSizeSum / counter);
			System.out.println("\n" + avgFreqReportLines + "  avg lines in frequency reports");
		}
		
		if (pureFreqReport) {
			System.out.println("\nPure Frequency Reports ---");
			for (String line : pureFreqReports) {
				System.out.println(line);
			}
		}
	}

}
