package fun.kolowert.aplayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import fun.kolowert.common.Combinator;
import fun.kolowert.common.GameType;
import fun.kolowert.common.HistHandler;
import fun.kolowert.serv.Timer;

public class APlay {

	private static final GameType GAME_TYPE = GameType.MAXI;
	private static final int PLAY_SET = 5;
	private static final int HIST_DEEP = 18;
	private static final int HIST_SHIFT = 0;
	private static final int HIST_SHIFTS = 20;
	private static final int REPORT_LIMIT = 2_000;

	private static final int WORKING_THREADS_AMOUNT = 3;

	public static void main(String[] args) {
		System.out.println("* Alpha Play * " + GAME_TYPE.name() + " * " + LocalDate.now());

		ParamSetA paramSet = new ParamSetA(GAME_TYPE, PLAY_SET, HIST_DEEP, HIST_SHIFT, REPORT_LIMIT);

		Timer timer = new Timer();

		APlay aPlay = new APlay();

		aPlay.playOne();

		aPlay.playMulty(paramSet);

		System.out.print("\naPlay finished ~ " + timer.reportExtended());
	}

	public void playOne() {
		System.out.println("~ playOne  ~ " + " playSet:" + PLAY_SET + " histDeep:" + HIST_DEEP + " histShift:"
				+ HIST_SHIFT + " reportLimit" + REPORT_LIMIT + "   " + LocalTime.now().toString().substring(0, 8));
		System.out.println(Combinator.reportCombinationsQuantity(PLAY_SET, GAME_TYPE.getGameSetSize()));

		HistHandler histHandler = new HistHandler(GAME_TYPE, HIST_DEEP, HIST_SHIFT);
		List<int[]> histBox = histHandler.getHistCombinations();
		int[] nextLine = histHandler.getNextLineOfHistBlock(HIST_SHIFT);
		Reporter reporter = new Reporter(GAME_TYPE, histBox, nextLine, REPORT_LIMIT);

		Combinator combinator = new Combinator(PLAY_SET, GAME_TYPE.getGameSetSize());

		while (!combinator.isFinished()) {
			int[] combination = combinator.makeNext();
			reporter.processCombination(combination);
		}

//		System.out.println("PlayCombinationsReport");
//		String playCombinationsReport = reporter.reportPlayCombinations();
//		System.out.println(playCombinationsReport);

		double[] frequencyes = reporter.countFrequencyes();
		System.out.println("Frequencyes Report on base line id:" + histBox.get(0)[0]);
		System.out.println(Reporter.reportFrequencyes(frequencyes));

		System.out.println("Ball sequence");
		int[] ballSequence = Reporter.extractBallSequence(frequencyes);
		System.out.println(Arrays.toString(ballSequence));

		System.out.println("Hits in ball sequence");
		int[] hits = countBallHitsInBallSequence(ballSequence, nextLine);
		System.out.println(Arrays.toString(hits));

		System.out.println("\n---------");
		System.out.println("base Line: " + Arrays.toString(histBox.get(0)));
		System.out.println("next Line: " + Arrays.toString(nextLine));
		System.out.println();
	}

	private int[] countBallHitsInBallSequence(int[] ballSequence, int[] nextLine) {
		int length = ballSequence.length;
		int[] result = new int[length];

		for (int i = 0; i < length; i++) {
			if (isHit(ballSequence[i], nextLine)) {
				++result[i];
			}
		}
		return result;
	}

	private boolean isHit(int ball, int[] nextLine) {
		for (int n : nextLine) {
			if (ball == n) {
				return true;
			}
		}
		return false;
	}

	public void playMulty(ParamSetA paramSet) {
		System.out.println("# playMulty  # " + " playSet:" + PLAY_SET + " histDeep:" + HIST_DEEP + " histShift:"
				+ " histLines:" + HIST_SHIFT + "/" + HIST_SHIFTS + " treads:" + WORKING_THREADS_AMOUNT + "   "
				+ LocalTime.now().toString().substring(0, 8));
		System.out.println(Combinator.reportCombinationsQuantity(PLAY_SET, GAME_TYPE.getGameSetSize()));

		List<int[]> histOrderResultTab = new ArrayList<>();
		int pause = 125 * PLAY_SET * PLAY_SET;

		// main cycle
		for (int indexHistShift = HIST_SHIFT + HIST_SHIFTS - 1; indexHistShift >= HIST_SHIFT; indexHistShift--) {

			int threadCounter = countWorkingThreds("alm");

			// wait if too lot threads ...
			while (threadCounter >= WORKING_THREADS_AMOUNT) {
				int fixedResultSize = histOrderResultTab.size();
				System.out.print(".");
				try {
					Thread.sleep(pause);
					if (fixedResultSize != histOrderResultTab.size()) {
						threadCounter -= histOrderResultTab.size() - fixedResultSize;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// do job
			Thread workingThread = new Thread(
					new ConcurentWorker(paramSet.getGameType(), paramSet.getPlaySet(), paramSet.getHistDeep(),
							indexHistShift, paramSet.getReportLimit(), histOrderResultTab),
					"almond" + indexHistShift);
			workingThread.start();

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
			int fixedResultSize = histOrderResultTab.size();
			System.out.print(".");
			try {
				Thread.sleep(pause);
				if (fixedResultSize != histOrderResultTab.size()) {
					threadCounter -= histOrderResultTab.size() - fixedResultSize;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		doPostCycleReport(histOrderResultTab);

	}

	private void doPostCycleReport(List<int[]> results) {
		System.out.println();
		System.out.println("histOrderResultTab");
		for (int[] hits : results) {
			System.out.println(Arrays.toString(hits));
		}
		int[] histOrderResultTabSum = new int[results.get(0).length];
		for (int[] hits : results) {
			for (int i = 0; i < hits.length; i++) {
				histOrderResultTabSum[i] += hits[i];
			}
		}
		System.out.println("Sum of histOrderResultTab");
		System.out.println(Arrays.toString(histOrderResultTabSum));
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

	public static void test1() {
		List<int[]> histBox = new ArrayList<>();
		histBox.add(new int[] { 1, 9, 30, 40, 45 });
		histBox.add(new int[] { 4, 5, 10, 15, 20 });
		histBox.add(new int[] { 5, 7, 12, 22, 33 });
		histBox.add(new int[] { 2, 8, 14, 24, 44 });
		histBox.add(new int[] { 3, 6, 16, 26, 36 });
		histBox.add(new int[] { 6, 19, 29, 35, 49 });
		histBox.add(new int[] { 7, 18, 28, 36, 48 });
		histBox.add(new int[] { 8, 17, 27, 37, 47 });
		histBox.add(new int[] { 9, 16, 26, 38, 46 });
		histBox.add(new int[] { 1, 15, 25, 39, 45 });

		int[] nextLine = new int[] { 1, 2, 3, 4, 5 };

		Reporter reporter = new Reporter(GameType.MAXI, histBox, nextLine, 3);

		List<int[]> combs = new ArrayList<>();
		combs.add(new int[] { 1, 11, 21, 31, 41 });
		combs.add(new int[] { 2, 12, 22, 32, 42 });
		combs.add(new int[] { 3, 13, 23, 33, 43 });
		combs.add(new int[] { 4, 14, 24, 34, 44 });
		combs.add(new int[] { 5, 15, 25, 35, 45 });
		combs.add(new int[] { 9, 16, 26, 36, 46 });

		for (int[] comb : combs) {
			reporter.processCombination(comb);
		}

		String playCombinationsReport = reporter.reportPlayCombinations();
		System.out.println(playCombinationsReport);
	}
}
