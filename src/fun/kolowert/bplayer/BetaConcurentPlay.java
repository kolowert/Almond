package fun.kolowert.bplayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReportPool;
import fun.kolowert.cplayer.Combinator;
import fun.kolowert.cplayer.FreqReporterOnPool;
import fun.kolowert.cplayer.PoolPlay;
import fun.kolowert.serv.Serv;
import fun.kolowert.serv.Timer;

public class BetaConcurentPlay {

	private static final GameType GAME_TYPE = GameType.SUPER;
	private static final int PLAY_SET = 7;
	private static final int HIST_DEEP = 45;
	private static final int HIST_SHIFT = 4;
	private static final int HIST_SHIFTS = 12;
	private static final int[] matchingMask = new int[] { 100, 100, 0, 0, 0, 0, 0, 0 };

	private static final int[] hitMaskIsolated = { 13, 26, 39, 52 };

	private static final int WORKING_THREADS_AMOUNT = 3;

	public static void main(String[] args) {
		System.out.println("* BetaConcurentPlay * " + GAME_TYPE.name() + " * " + LocalDate.now());
		BetaConcurentPlay mainPlayer = new BetaConcurentPlay();
		ParamSet paramSet = new ParamSet(GAME_TYPE, PLAY_SET, HIST_DEEP, HIST_SHIFT, HIST_SHIFTS, matchingMask,
				hitMaskIsolated);
		Timer timer = new Timer();

		boolean doit = false;
		boolean display = true;
		mainPlayer.poolPlay(doit, display);

		mainPlayer.multiPlay(paramSet);

		System.out.print("\nFINISH ~ " + timer.reportExtended());
	}

	public void poolPlay(boolean doit, boolean display) {
		if (!doit)
			return;

		Timer timer = new Timer();

		System.out.println("~ Concurent Single ~ " + " playSet:" + PLAY_SET + " histDeep:" + HIST_DEEP
				+ " matchingMask:" + Arrays.toString(matchingMask) + " histLine:" + HIST_SHIFT + "   "
				+ LocalTime.now().toString().substring(0, 8));
		System.out.println(Combinator.reportCombinationsQuantity(PLAY_SET, GAME_TYPE.getGameSetSize()));

		PoolPlay poolPlay = new PoolPlay(GAME_TYPE, PLAY_SET, matchingMask);
		MatchingReportPool pool = poolPlay.makeMatchingReportPool(HIST_DEEP, HIST_SHIFT, display);
		System.out.println(">> " + pool.size() + " lines in frequency reports");

		FreqReporterOnPool freqReporter = new FreqReporterOnPool(GAME_TYPE, pool);
		freqReporter.displayFrequencyReports(HIST_SHIFT);

		System.out.println("poolPlay time - - -> " + timer.reportExtended() + "\n");
	}
	// ----------------------------------------------------------------------------------------------------------------

	public void multiPlay(ParamSet paramSet) {
		System.out.println("# Concurent Multy # " + " playSet:" + PLAY_SET + " histDeep:" + HIST_DEEP
				+ " matchingMask:" + Arrays.toString(matchingMask) + " histLines:" + HIST_SHIFT + "/" + HIST_SHIFTS
				+ " treads:" + WORKING_THREADS_AMOUNT + "   " + LocalTime.now().toString().substring(0, 8));
		System.out.println(Combinator.reportCombinationsQuantity(PLAY_SET, GAME_TYPE.getGameSetSize()));

		List<ResultSet> results = new ArrayList<>();
		int pause = 125 * PLAY_SET * PLAY_SET;

		// main cycle
		for (int indexHistShift = HIST_SHIFT + HIST_SHIFTS - 1; indexHistShift >= HIST_SHIFT; indexHistShift--) {

			int threadCounter = countWorkingThreds("alm");
			//System.out.println(" ~" + threadCounter + "~ ");

			// wait if too lot threads ...
			while (threadCounter > WORKING_THREADS_AMOUNT - 1) {
				int fixedResultSize = results.size();
				System.out.print(".");
				try {
					Thread.sleep(pause);
					if (fixedResultSize != results.size()) {
						threadCounter -= results.size() - fixedResultSize;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// do job
			Thread thread = new Thread(new ResultMaker(paramSet, indexHistShift, results), "almond" + indexHistShift);
			thread.start();

			System.out.print(indexHistShift + ".");

			// just small pause for smooth running
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // after main cycle

		// wait to end all working threads
		System.out.println();
		int threadCounter = countWorkingThreds("alm");
		while (threadCounter > 0) {
			int fixedResultSize = results.size();
			System.out.print(".");
			try {
				Thread.sleep(pause);
				if (fixedResultSize != results.size()) {
					threadCounter -= results.size() - fixedResultSize;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		doPostCycleReport(results);

	}

	private int countWorkingThreds(String namePrefix) {
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		int threadCounter = 0;
		for (Thread th : threadSet) {
			if (th.getName().substring(0, 3).equals(namePrefix)) {
				++threadCounter;
				//System.out.print(th.getName() + " ");
			}
		}
		return threadCounter;
	}

	private void doPostCycleReport(List<ResultSet> results) {
		results.sort(null);
		System.out.println("\nresults size = " + results.size());
		int poolSizeSum = 0;
		System.out.println("\nPure Frequency Reports");
		for (ResultSet rs : results) {
			System.out.println(
					FreqReporterOnPool.reportPureFrequencyReports(rs.getFrequencyReport(), rs.getIndexHistShift()));
			poolSizeSum += rs.getPoolSize();
		}
		System.out.println(pureHead(GAME_TYPE, hitMaskIsolated));

		if (!results.isEmpty()) {
			System.out.println("\navg pool size: " + ((int) 1.0 * poolSizeSum / results.size()));
		}

		System.out.println("\nIsolated hit reports (hit.range)");
		for (ResultSet rs : results) {
			System.out.println(
					Serv.normIntX(rs.getIndexHistShift(), 3, "0") + " " + Arrays.toString(rs.getIsolatedHitReport()));
		}
		displayHitReportsResume(results);
	}

	private static void displayHitReportsResume(List<ResultSet> results) {
		if (results == null || results.isEmpty()) {
			System.out.println("can't displayHitReportsResume with bad argument");
			return;
		}

		int rows = hitMaskIsolated.length;
		int[] hitsSums = new int[rows];
		for (ResultSet rs : results) {
			double[] hitLine = rs.getIsolatedHitReport();
			for (int j = 0; j < rows; j++) {
				hitsSums[j] += (int) hitLine[j];
			}
		}

		StringBuilder head = new StringBuilder("\nhead  |  ");
		for (int i = 0; i < hitMaskIsolated.length; i++) {
			head.append(Serv.normIntX(hitMaskIsolated[i], 2, "0")).append("  |  |  ");
		}
		System.out.println(head.toString().substring(0, head.length() - 4));

		StringBuilder hits = new StringBuilder("hits  | ");
		for (int i = 0; i < hitMaskIsolated.length; i++) {
			hits.append(Serv.normIntX(hitsSums[i], 3, " ")).append("  |  | ");
		}
		System.out.println(hits.toString().substring(0, hits.length() - 3));

		StringBuilder coef = new StringBuilder("coef  |");
		double[] coefValues = new double[hitMaskIsolated.length];
		int lines = results.size();
		for (int i = 0; i < hitMaskIsolated.length; i++) {
			coefValues[i] = 1.0 * hitsSums[i] / lines + 0.00005;
			coef.append(Serv.normDoubleX(coefValues[i], 4)).append("|  |");
		}
		System.out.println(coef.toString().substring(0, coef.length() - 3));

		System.out.println("\nsorted hit reports resume");

		// make sorted coefficient values..
		double[] tranceCoef = new double[coefValues.length];
		for (int i = 0; i < tranceCoef.length; i++) {
			tranceCoef[i] = (int) (10_000 * coefValues[i]) + 0.01 * hitMaskIsolated[i];
		}
		// ..and display
		Arrays.sort(tranceCoef);
		//System.out.println(Arrays.toString(tranceCoef)); //TODO 
		StringBuilder sb = new StringBuilder();
		int step = hitMaskIsolated[0] - 1;
		for (int i = tranceCoef.length - 1; i >= 0; i--) {
			int n = (int) (0.005 + 100 * (tranceCoef[i] - (int) tranceCoef[i]));
			double f = 0.0001 * tranceCoef[i];
			sb.append("(").append(Serv.normIntX(n - step, 2, "0")).append("-").append(Serv.normIntX(n, 2, "0"))
					.append(") ").append(Serv.normDoubleX(0.005 + f, 2)).append("  ");
		}
		System.out.println(sb);

	}

	private static String pureHead(GameType gameType, int[] mask) {
		StringBuilder sb = new StringBuilder("--- |");
		int maskIndex = 0;
		for (int i = 1; i <= gameType.getGameSetSize(); i++) {
			sb.append(Serv.normIntX(i, 2, "0"));
			if (i == mask[maskIndex]) {
				sb.append("|");
				++maskIndex;
			} else {
				sb.append("-");	
			}
		}
		return sb.toString();
	}

}
