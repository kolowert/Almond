package fun.kolowert.bplayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReportPool;
import fun.kolowert.cplayer.Combinator;
import fun.kolowert.cplayer.FreqReporterOnPoolSimple;
import fun.kolowert.cplayer.HitReporter;
import fun.kolowert.cplayer.PoolPlay;
import fun.kolowert.serv.Serv;
import fun.kolowert.serv.Timer;

public class BetaConcurentPlay {

	private static final GameType GAME_TYPE = GameType.SUPER;
	private static final int PLAY_SET = 4;
	private static final int HIST_DEEP = 52;
	private static final int HIST_SHIFT = 1;
	private static final int HIST_SHIFTS = 3;
	private static final int[] matchingMask = new int[] { 100, 100, 0, 0, 0 };

	private static final int[] hitMaskIsolated = { 13, 26, 39, 52 };

	public static void main(String[] args) {
		System.out.println("* BetaConcurentPlay * " + GAME_TYPE.name() + " * " + LocalDate.now());
		BetaConcurentPlay mainPlayer = new BetaConcurentPlay();
		Timer timer = new Timer();

//		boolean display = false;
//		mainPlayer.poolPlay(display);

		mainPlayer.multiPlay();

		System.out.println("\nFINISH ~ " + timer.reportExtended());
	}

	public void poolPlay(boolean display) {
		Timer timer = new Timer();

		System.out.println("~ Concurent Single ~ " + " playSet:" + PLAY_SET + " histDeep:" + HIST_DEEP
				+ " matchingMask:" + Arrays.toString(matchingMask) + " histLine:" + HIST_SHIFT + "   "
				+ LocalTime.now().toString().substring(0, 8));
		System.out.println(Combinator.reportCombinationsQuantity(PLAY_SET, GAME_TYPE.getGameSetSize()));

		PoolPlay poolPlay = new PoolPlay(GAME_TYPE, PLAY_SET, matchingMask);
		MatchingReportPool pool = poolPlay.makeMatchingReportPool(HIST_DEEP, HIST_SHIFT, display);
		System.out.println(">> " + pool.size() + " lines in frequency reports");

		FreqReporterOnPoolSimple freqReporter = new FreqReporterOnPoolSimple(GAME_TYPE, pool);
		freqReporter.displayFrequencyReports(HIST_SHIFT);

		System.out.println("poolPlay time - - -> " + timer.reportExtended() + "\n");
	}
	// ----------------------------------------------------------------------------------------------------------------

	public void multiPlay() {
		System.out.println("# Concurent Multy # " + " playSet:" + PLAY_SET + " histDeep:" + HIST_DEEP
				+ " matchingMask:" + Arrays.toString(matchingMask) + " histLines:" + HIST_SHIFT + "/" + HIST_SHIFTS
				+ "   " + LocalTime.now().toString().substring(0, 8));
		System.out.println(Combinator.reportCombinationsQuantity(PLAY_SET, GAME_TYPE.getGameSetSize()));

		List<ResultSet> results = new ArrayList<>();

		// main cycle
		for (int indexHistShift = HIST_SHIFT + HIST_SHIFTS - 1; indexHistShift >= HIST_SHIFT; indexHistShift--) {

			ResultSet result = doCoreCalc(indexHistShift);
			results.add(result);

		}

		doPostCycleReport(results);

	}

	private ResultSet doCoreCalc(int indexHistShift) {
		// TODO

		PoolPlay poolPlay = new PoolPlay(GAME_TYPE, PLAY_SET, matchingMask);
		MatchingReportPool pool = poolPlay.makeMatchingReportPool(HIST_DEEP, indexHistShift, false);

		FreqReporterOnPoolSimple freqReporter = new FreqReporterOnPoolSimple(GAME_TYPE, pool);
		double[] frequencyReport = freqReporter.getFrequencyReport();

		double[] hitReport = HitReporter.makeHitReports(GAME_TYPE, HIST_DEEP, indexHistShift, frequencyReport,
				hitMaskIsolated);
		// System.out.println(Arrays.toString(hitReport)); // debuggin
		/* String hitReportReport = */HitReporter.reportIsolatedHitReports(hitReport);
		// System.out.println("hitReport isltd " + Serv.normIntX(indexHistShift, 2, " ")
		// + ": " + hitReportReport + "");
		// System.out.println(Arrays.toString(hitReport)); // debuggin

		// TODO
		return new ResultSet(indexHistShift, frequencyReport, hitReport, pool.size());
	}

	private void doPostCycleReport(List<ResultSet> results) {
		System.out.println("results.size(): " + results.size());
		int poolSizeSum = 0;
		System.out.println("\nFrequency reports");
		for (ResultSet rs : results) {
			System.out.println(Arrays.toString(rs.getFrequencyReport()));
			poolSizeSum += rs.getPoolSize();
		}
		System.out.println("\navg pool size: " + ((int) 1.0 * poolSizeSum / results.size()));
		System.out.println("\nIsolated hit reports");
		for (ResultSet rs : results) {
			System.out.println(Arrays.toString(rs.getIsolatedHitReport()));
		}
	}

//	private void doPostCycleReportOld() {
		// TODO
//		if (isolatedHits && isolatedHitsCounter != 0) {
//			StringBuilder head = new StringBuilder("\n                        --");
//			for (int i = 0; i < hitMaskIsolated.length; i++) {
//				head.append(Serv.normIntX(hitMaskIsolated[i], 2, "0")).append("--       --");
//			}
//			System.out.println(head);
//
//			double[] coefReport = new double[isolatedHitAvgCoefSum.length];
//			StringBuilder sb = new StringBuilder("IsolatedHit avgCoef >>  ");
//			for (int i = 0; i < isolatedHitAvgCoefSum.length; i++) {
//				double avgCoef = isolatedHitAvgCoefSum[i] / isolatedHitsCounter;
//				sb.append(Serv.normDoubleX(avgCoef, 4)).append("       ");
//				coefReport[i] = (int) (10000 * avgCoef) + 0.01 * hitMaskIsolated[i];
//			}
//			System.out.println(sb);
//			Arrays.sort(coefReport);
//			displayCoefReport(coefReport);
//		}
//
//		if (counter != 0) {
//			int avgFreqReportLines = (int) (1.0 * poolSizeSum / counter);
//			System.out.println("\n" + avgFreqReportLines + "  avg lines in frequency reports");
//		}
//
//		if (pureFreqReport) {
//			System.out.println("\nPure Frequency Reports ---");
//			for (String line : pureFreqReports) {
//				System.out.println(line);
//			}
//			System.out.println(pureHead(GAME_TYPE));
//		}
//	}

//	private static String pureHead(GameType gameType) {
//		StringBuilder sb = new StringBuilder("-----");
//		for (int i = 1; i <= gameType.getGameSetSize(); i++) {
//			sb.append(Serv.normIntX(i, 2, "0")).append("-");
//		}
//		return sb.toString();
//	}
//
//	private static void displayCoefReport(double[] coefReport) {
//		StringBuilder sb = new StringBuilder();
//		int step = hitMaskIsolated[0] - 1;
//		for (int i = coefReport.length - 1; i >= 0; i--) {
//			int n = (int) (100 * (0.005 + coefReport[i] - (int) coefReport[i]));
//			double coef = 0.0001 * step * (int) coefReport[i];
//			sb.append("(").append(Serv.normIntX(n - step, 2, "0")).append("-").append(Serv.normIntX(n, 2, "0"))
//					.append(") ").append(Serv.normDoubleX(coef, 2)).append("  ");
//		}
//
//		System.out.println("sorted                  " + sb);
//	}

}
