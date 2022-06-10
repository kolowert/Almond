package fun.kolowert.aplayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fun.kolowert.common.Combinator;
import fun.kolowert.common.GameType;
import fun.kolowert.common.HistHandler;
import fun.kolowert.serv.Timer;

public class APlay {

	private static final GameType GAME_TYPE = GameType.MAXI;
	private static final int PLAY_SET = 5;
	private static final int HIST_DEEP = 100;
	private static final int HIST_SHIFT = 3;
	private static final int REPORT_LIMIT = 100;

	public static void main(String[] args) {
		System.out.println("* Alpha Play * " + GAME_TYPE.name() + " * " + LocalDate.now());
		Timer timer = new Timer();

		APlay aPlay = new APlay();

		aPlay.playOne();

		System.out.print("\naPlay finished ~ " + timer.reportExtended());
	}

	public void playOne() {
		System.out.println("~ playOne  ~ " + " playSet:" + PLAY_SET + " histDeep:" + HIST_DEEP + " histShift:"
				+ HIST_SHIFT + "   " + LocalTime.now().toString().substring(0, 8));
		System.out.println(Combinator.reportCombinationsQuantity(PLAY_SET, GAME_TYPE.getGameSetSize()));

		HistHandler histHandler = new HistHandler(GAME_TYPE, HIST_DEEP, HIST_SHIFT);
		List<int[]> histBox = histHandler.getHistCombinations();
		int[] nextLine = new int[GAME_TYPE.getPlaySetSize()];
		Reporter reporter = new Reporter(GameType.MAXI, histBox, nextLine, REPORT_LIMIT);

		Combinator combinator = new Combinator(GAME_TYPE.getCombSetSize(), GAME_TYPE.getGameSetSize());

		while (!combinator.isFinished()) {
			int[] combination = combinator.makeNext();
			reporter.processCombination(combination);
		}

		String playCombinationsReport = reporter.reportPlayCombinations();
		System.out.println(playCombinationsReport);
		System.out.println("next Line: " + Arrays.toString(nextLine));
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
