package fun.kolowert.cplayer;

import java.time.LocalDate;
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

	private static final GameType GAME_TYPE = GameType.KENO;
	private static final int PLAY_SET = 4;
	private static final int HIST_DEEP = 18;
	private static final int HIST_SHIFT = 51;
	private static final int HIST_SHIFTS = 10;
	private static final int[] matchingMask = new int[] { 100, 100, 0, 0, 0 };

	private static final int[] hitMaskPlain = { 2, 3, 4, 5, 6, 8, 10, 12, 18 };
	//private static final int[] hitMaskIsolated = { 16, 32, 48, 64, 80 };
	private static final int[] hitMaskIsolated = { 8, 16, 24, 32, 40, 48, 56, 64, 72, 80 };
//	private static final int[] hitMaskIsolated = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30,
//			32, 34, 36, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76, 78, 80 };
	// private static final int[] hitMaskIsolated = { 4, 8, 12, 16, 20, 24, 28, 32,
	// 36, 40, 44, 48, 52, 56, 60, 64, 68, 72, 76, 80 };

	public static void main(String[] args) {
		System.out.println("* BetaPlay * " + GAME_TYPE.name() + " * " + LocalDate.now());
		Timer timer = new Timer();

		boolean display = false;
		poolPlay(display);

		boolean plainHits = false;
		boolean isolatedHits = true;
		boolean pureFreqReport = false;
		multiPlay(plainHits, isolatedHits, pureFreqReport);

		System.out.println("\nFINISH ~ " + timer.reportExtended());
	}

	public static void poolPlay(boolean display) {
		Timer timer = new Timer();

		System.out.println("~ Single ~ " + " playSet:" + PLAY_SET + " histDeep:" + HIST_DEEP + " matchingMask:"
				+ Arrays.toString(matchingMask) + " histLine:" + HIST_SHIFT + "   "
				+ LocalTime.now().toString().substring(0, 8));
		System.out.println(Combinator.reportCombinationsQuantity(PLAY_SET, GAME_TYPE.getGameSetSize()));

		PoolPlay poolPlay = new PoolPlay(GAME_TYPE, PLAY_SET, matchingMask);
		MatchingReportPool pool = poolPlay.makeMatchingReportPool(HIST_DEEP, HIST_SHIFT, display);
		System.out.println(">> " + pool.size() + " lines in frequency reports");

		FreqReporterOnPoolSimple freqReporter = new FreqReporterOnPoolSimple(GAME_TYPE, pool);
		double[] frequencyReport = freqReporter.getFrequencyReport();
		freqReporter.displayFrequencyReports(HIST_SHIFT);

		HitReporter hitReporter = new HitReporter();
		double[] hitReports = hitReporter.makeHitReports(GAME_TYPE, HIST_DEEP, HIST_SHIFT, frequencyReport,
				hitMaskPlain);
		String hitReportsReport = hitReporter.reportHitReports(hitReports);
		System.out.println("hitReport S " + HIST_SHIFT + ": " + hitReportsReport + "\n");

		System.out.println("poolPlay time - - -> " + timer.reportExtended() + "\n");
	}
	// ----------------------------------------------------------------------------------------------------------------

	public static void multiPlay(boolean plainHits, boolean isolatedHits, boolean pureFreqReport) {
		System.out.println("# Multy # " + " playSet:" + PLAY_SET + " histDeep:" + HIST_DEEP + " matchingMask:"
				+ Arrays.toString(matchingMask) + " histLines:" + HIST_SHIFT + "/" + HIST_SHIFTS + "   "
				+ LocalTime.now().toString().substring(0, 8));
		System.out.println(Combinator.reportCombinationsQuantity(PLAY_SET, GAME_TYPE.getGameSetSize()));

		List<String> pureFreqReports = new ArrayList<>();
		HitReporter hitReporter = new HitReporter();

		int poolSizeSum = 0;
		double[] plainHitAvgCoefSum = new double[hitMaskPlain.length];
		double[] isolatedHitAvgCoefSum = new double[hitMaskIsolated.length];
		int counter = 0;
		int plainHitsCounter = 0;
		int isolatedHitsCounter = 0;
		// main cycle
		for (int indexHistShift = HIST_SHIFT + HIST_SHIFTS - 1; indexHistShift >= HIST_SHIFT; indexHistShift--) {
			PoolPlay poolPlay = new PoolPlay(GAME_TYPE, PLAY_SET, matchingMask);
			MatchingReportPool pool = poolPlay.makeMatchingReportPool(HIST_DEEP, indexHistShift, false);
			poolSizeSum += pool.size();

			FreqReporterOnPoolSimple freqReporter = new FreqReporterOnPoolSimple(GAME_TYPE, pool);
			double[] frequencyReport = freqReporter.getFrequencyReport();

			if (pureFreqReport) {
				pureFreqReports.add(freqReporter.reportPureFrequencyReports(indexHistShift));
			}

			if (plainHits) {
				double[] hitReports = hitReporter.makeHitReports(GAME_TYPE, HIST_DEEP, indexHistShift,
						frequencyReport, hitMaskPlain);
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
				double[] hitReports = hitReporter.makeHitReports(GAME_TYPE, HIST_DEEP, indexHistShift,
						frequencyReport, hitMaskIsolated);
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
		} // end of main cycle

		if (plainHits && plainHitsCounter != 0) {
			StringBuilder sb = new StringBuilder("PlainHit    avgCoef >>  ");
			for (int i = 0; i < plainHitAvgCoefSum.length; i++) {
				sb.append(Serv.normDoubleX(plainHitAvgCoefSum[i] / plainHitsCounter, 4)).append("         ");
			}
			System.out.println("\n" + sb);
		}

		if (isolatedHits && isolatedHitsCounter != 0) {
			StringBuilder head = new StringBuilder("\n                        --");
			for (int i = 0; i < hitMaskIsolated.length; i++) {
				head.append(Serv.normIntX(hitMaskIsolated[i], 2, "0")).append("--       --");
			}
			System.out.println(head);

			double[] coefReport = new double[isolatedHitAvgCoefSum.length];
			StringBuilder sb = new StringBuilder("IsolatedHit avgCoef >>  ");
			for (int i = 0; i < isolatedHitAvgCoefSum.length; i++) {
				double avgCoef = isolatedHitAvgCoefSum[i] / isolatedHitsCounter;
				sb.append(Serv.normDoubleX(avgCoef, 4)).append("       ");
				coefReport[i] = (int) (10000 * avgCoef) + 0.01 * hitMaskIsolated[i];
			}
			System.out.println(sb);
			Arrays.sort(coefReport);
			displayCoefReport(coefReport);
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
			System.out.println(pureHead(GAME_TYPE));
		}
	}

	private static String pureHead(GameType gameType) {
		StringBuilder sb = new StringBuilder("-----");
		for (int i = 1; i <= gameType.getGameSetSize(); i++) {
			sb.append(Serv.normIntX(i, 2, "0")).append("-");
		}
		return sb.toString();
	}

	private static void displayCoefReport(double[] coefReport) {
		StringBuilder sb = new StringBuilder();
		int step = hitMaskIsolated[0];
		for (int i = coefReport.length - 1; i >= 0; i--) {
			int n = (int) (100 * (0.005 + coefReport[i] - (int) coefReport[i]));
			double coef = 0.0001 * step * (int) coefReport[i];
			sb	.append("(")
				.append(Serv.normIntX(n - step, 2, "0"))
				.append("-")
				.append(Serv.normIntX(n, 2, "0"))
				.append(") ")
				.append(Serv.normDoubleX(coef, 2))
				.append("  ");
		}

		System.out.println("sorted                  " + sb);
	}

}
