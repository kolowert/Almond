package fun.kolowert.aplayer;

import java.util.List;

import fun.kolowert.common.Combinator;
import fun.kolowert.common.GameType;
import fun.kolowert.common.HistHandler;

public class ConcurentWorker implements Runnable {

	private final GameType gameType;
	private final SortType sortType;
	private final int playSet;
	private final int histDeep;
	private final int histShift;
	private final int reportLimit;
	List<int[]> histOrderResultTab;

	public ConcurentWorker(GameType gameType, SortType sortType, int playSet, int histDeep, int histShift,
			int reportLimit, List<int[]> histOrderResultTab) {
		this.gameType = gameType;
		this.sortType = sortType;
		this.playSet = playSet;
		this.histDeep = histDeep;
		this.histShift = histShift;
		this.reportLimit = reportLimit;
		this.histOrderResultTab = histOrderResultTab;
	}

	@Override
	public void run() {
		int[] histOrder = countHitsOrder();
		histOrderResultTab.add(histOrder);
	}

	public int[] countHitsOrder() {
		HistHandler histHandler = new HistHandler(gameType, histDeep, histShift);
		List<int[]> histBox = histHandler.getHistCombinations();
		int[] nextLine = histHandler.getNextLineOfHistBlock(histShift);
		Reporter reporter = new Reporter(gameType, histBox, nextLine, reportLimit);
		Combinator combinator = new Combinator(playSet, gameType.getGameSetSize());
		while (!combinator.isFinished()) {
			int[] combination = combinator.makeNext();
			reporter.processCombination(combination, sortType);
		}
		double[] frequencyes = reporter.countFrequencyes();
		int[] ballSequence = Reporter.extractBallSequence(frequencyes);
		return countBallHitsInBallSequence(ballSequence, nextLine);
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

}
