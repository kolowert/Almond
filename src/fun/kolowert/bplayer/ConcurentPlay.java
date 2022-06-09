package fun.kolowert.bplayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import fun.kolowert.common.Combinator;
import fun.kolowert.common.FreqReporterOnPool;
import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReportPool;
import fun.kolowert.cplayer.PoolPlay;
import fun.kolowert.serv.Serv;
import fun.kolowert.serv.Sounder;
import fun.kolowert.serv.Timer;

public class ConcurentPlay {

	private static final GameType GAME_TYPE = GameType.KENO;
	private static final int PLAY_SET = 5;
	private static final int HIST_DEEP = 40;
	private static final int HIST_SHIFT = 0;
	private static final int HIST_SHIFTS = 24;
	private static final int[] matchingMask = new int[] { 100, 100, 8, 0, 0, 0 };

	private static final int[] hitRangeMask = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
			31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
			51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70,
			71, 72, 73, 74, 75, 76, 77, 78, 79, 80 };

	private static final int WORKING_THREADS_AMOUNT = 3;

	public static void main(String[] args) {
		System.out.println("* BetaConcurentPlay * " + GAME_TYPE.name() + " * " + LocalDate.now());
		ConcurentPlay mainPlayer = new ConcurentPlay();
		ParamSet paramSet = new ParamSet(GAME_TYPE, PLAY_SET, HIST_DEEP, HIST_SHIFT, HIST_SHIFTS, matchingMask,
				hitRangeMask);
		Timer timer = new Timer();

		boolean doit = false;
		boolean display = true;
		mainPlayer.poolPlay(doit, display);

		mainPlayer.multiPlay(paramSet);

		System.out.print("\nFINISH ~ " + timer.reportExtended());
		Sounder.beep();
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
			// System.out.println(" ~" + threadCounter + "~ ");

			// wait if too lot threads ...
			while (threadCounter >= WORKING_THREADS_AMOUNT) {
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
			Thread resultMakerThread = new Thread(new ResultMaker(paramSet, indexHistShift, results),
					"almond" + indexHistShift);
			resultMakerThread.start();

			System.out.print(indexHistShift + ":");

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
				// System.out.print(th.getName() + " ");
			}
		}
		return threadCounter;
	}

	private void doPostCycleReport(List<ResultSet> results) {
		if (!results.isEmpty()) {
			int poolSizeSum = 0;
			for (ResultSet rs : results) {
				poolSizeSum += rs.getPoolSize();
			}
			System.out.println("\naverage pool size: " + ((int) 1.0 * poolSizeSum / results.size()));
		}

		results.sort(null);
		System.out.println("\nresults size = " + results.size());
		System.out.println("\nPure Frequency Reports");
		System.out.println(pureHead(GAME_TYPE, hitRangeMask));
		for (ResultSet rs : results) {
			System.out.println(
					FreqReporterOnPool.reportPureFrequencyReports(rs.getFrequencyReport(), rs.getIndexHistShift()));
		}
		System.out.println(pureHead(GAME_TYPE, hitRangeMask));

		System.out.println("\ntableOfHitsOnFrequency");
		System.out.println(pureHead(GAME_TYPE, hitRangeMask));
		List<int[]> hitPozysionsTab = new ArrayList<>();
		List<String> tableOfHitsOnFrequency = HitAnalizer.makeTableOfHitsOnFrequency(GAME_TYPE, results,
				hitPozysionsTab, true, false);

		for (String hitLine : tableOfHitsOnFrequency) {
			System.out.println(hitLine);
		}
		System.out.println(pureHead(GAME_TYPE, hitRangeMask));

		List<int[]> hitsOnRenges = calculateHitsOnRenges(hitPozysionsTab, hitRangeMask);

		displayTab(hitsOnRenges, "\nHits on Renges");

		displayHitsOnRangesResume(hitsOnRenges, hitRangeMask);

	}

	private static List<int[]> calculateHitsOnRenges(List<int[]> hitPozysionsTab, int[] mask) {
		List<int[]> result = new ArrayList<>();

		for (int[] hitPozysionsLine : hitPozysionsTab) {
			int[] hits = new int[mask.length + 1];
			hits[0] = hitPozysionsLine[0];
			for (int maskIndex = 0; maskIndex < mask.length; maskIndex++) {
				for (int i = 1; i < hitPozysionsLine.length; i++) {
					if (hitPozysionsLine[i] > 0 && hitPozysionsLine[i] <= mask[maskIndex]) {
						++hits[maskIndex + 1];
					}
				}
			}
			int hitSum = 0;
			for (int i = 2; i < hits.length; i++) {
				hitSum += hits[i - 1];
				hits[i] = hits[i] - hitSum;
			}
			result.add(hits);
		}
		return result;
	}

	private static void displayTab(List<int[]> tab, String title) {
		StringBuilder result = new StringBuilder();
		result.append(title).append(System.lineSeparator());
		for (int[] line : tab) {
			StringBuilder h = new StringBuilder(Serv.normIntX(line[0], 3, "0") + "   ");
			for (int i = 1; i < line.length; i++) {
				h.append(Serv.normIntX(line[i], 2, " ")).append("  ");
			}
			result.append(h).append(System.lineSeparator());
		}
		System.out.println(result);
	}

	private static void displayHitsOnRangesResume(List<int[]> hitsOnRenges, int[] mask) {
		int rows = hitRangeMask.length;
		int[] hitsSums = new int[rows];
		for (int[] line : hitsOnRenges) {
			for (int j = 0; j < rows; j++) {
				hitsSums[j] += line[j + 1];
			}
		}

		System.out.println("\nHits On Ranges Resume");

		StringBuilder head = new StringBuilder("head  |  ");
		for (int i = 0; i < hitRangeMask.length; i++) {
			head.append(Serv.normIntX(hitRangeMask[i], 2, "0")).append("  |  |  ");
		}
		System.out.println(head.toString().substring(0, head.length() - 4));

		StringBuilder hits = new StringBuilder("hits  | ");
		for (int i = 0; i < hitRangeMask.length; i++) {
			hits.append(Serv.normIntX(hitsSums[i], 3, " ")).append("  |  | ");
		}
		System.out.println(hits.toString().substring(0, hits.length() - 3));

		StringBuilder coef = new StringBuilder("coef  |");
		double[] coefValues = new double[hitRangeMask.length];
		int lines = hitsOnRenges.size();
		for (int i = 0; i < hitRangeMask.length; i++) {
			coefValues[i] = 1.0 * hitsSums[i] / lines + 0.00005;
			coef.append(Serv.normDoubleX(coefValues[i], 4)).append("|  |");
		}
		System.out.println(coef.toString().substring(0, coef.length() - 3));

		System.out.println("\nFinal resume");

		// make sorted coefficient values..
		double[] tranceCoef = new double[coefValues.length];
		for (int i = 0; i < tranceCoef.length; i++) {
			tranceCoef[i] = (int) (10_000 * coefValues[i]) + 0.01 * hitRangeMask[i];
		}
		// ..and display
		Arrays.sort(tranceCoef);
		StringBuilder sb = new StringBuilder();
		int step = hitRangeMask[0] - 1;
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
