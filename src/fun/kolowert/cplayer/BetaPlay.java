package fun.kolowert.cplayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fun.kolowert.common.Combinator;
import fun.kolowert.common.FreqReporterOnPool;
import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReportPool;
import fun.kolowert.serv.Serv;
import fun.kolowert.serv.Timer;

public class BetaPlay {

	private static final GameType GAME_TYPE = GameType.SUPER;
	private static final int PLAY_SET = 5;
	private static final int HIST_DEEP = 52;
	private static final int HIST_SHIFT = 1;
	private static final int HIST_SHIFTS = 24;
	private static final int[] matchingMask = new int[] { 100, 100, 0, 0, 0, 0 };

	private static final int[] hitMaskPlain = { 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 52 };
	
	private static final int[] hitMaskIsolated = { 13, 26, 39, 52 };
	//private static final int[] hitMaskIsolated = { 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 52 };

	public static void main(String[] args) {
		System.out.println("* BetaPlay * " + GAME_TYPE.name() + " * " + LocalDate.now());
		Timer timer = new Timer();

//		boolean display = false;
//		poolPlay(display);

		boolean plainHits = false;
		boolean isolatedHits = true;
		boolean pureFreqReport = true;
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

		FreqReporterOnPool freqReporter = new FreqReporterOnPool(GAME_TYPE, pool);
		double[] frequencyReport = freqReporter.getFrequencyReport();
		freqReporter.displayFrequencyReports(HIST_SHIFT);

		double[] hitReports = HitReporter.makeHitReports(GAME_TYPE, HIST_DEEP, HIST_SHIFT, frequencyReport,
				hitMaskPlain);
		String hitReportsReport = HitReporter.reportHitReports(hitReports);
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

			FreqReporterOnPool freqReporter = new FreqReporterOnPool(GAME_TYPE, pool);
			double[] frequencyReport = freqReporter.getFrequencyReport();

			if (pureFreqReport) {
				pureFreqReports.add(freqReporter.reportPureFrequencyReports(indexHistShift));
			}

			if (plainHits) {
				double[] hitReports = HitReporter.makeHitReports(GAME_TYPE, HIST_DEEP, indexHistShift,
						frequencyReport, hitMaskPlain);
				String hitReportsReport = HitReporter.reportHitReports(hitReports);
				System.out.println(
						"hitReport plain " + Serv.normIntX(indexHistShift, 2, " ") + ":  " + hitReportsReport + "");

				for (int i = 0; i < plainHitAvgCoefSum.length; i++) {
					plainHitAvgCoefSum[i] += 1.0 * (int) hitReports[i]
							/ (100.0 * (hitReports[i] - (int) hitReports[i]));
				}
				++plainHitsCounter;
			}

			if (isolatedHits) {
				double[] hitReports = HitReporter.makeHitReports(GAME_TYPE, HIST_DEEP, indexHistShift,
						frequencyReport, hitMaskIsolated);
				String hitReportsReport = HitReporter.reportIsolatedHitReports(hitReports);
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
		int step = hitMaskIsolated[0] - 1;
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
