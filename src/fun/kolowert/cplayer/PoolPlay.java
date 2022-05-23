package fun.kolowert.cplayer;

import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.HistHandler;
import fun.kolowert.common.MatchingCalculator;
import fun.kolowert.common.MatchingReportPool;
import fun.kolowert.serv.Serv;

public class PoolPlay {

	private final GameType gameType;
	private final int combSetSize;
	private final int gameSetSize;
	private final int[] matchingMask;

	public PoolPlay(GameType gameType, int combSize, int[] matchingMask) {
		this.gameType = gameType;
		this.combSetSize = combSize;
		this.gameSetSize = gameType.getGameSetSize();
		this.matchingMask = matchingMask;
	}

	public MatchingReportPool makeMatchingReportPool(int histDeep, int histShift, boolean display) {
		Combinator combinator = new Combinator(combSetSize, gameSetSize);
		int combinations = (int) Combinator.calculateCombinations(combSetSize, gameSetSize);
		HistHandler histHandler = new HistHandler(gameType, histDeep, histShift);
		List<int[]> histCombinations = histHandler.getHistCombinations();

		MatchingReportPool pool = new MatchingReportPool(gameType, histCombinations, 4096);

		int counter = 0;
		while (!combinator.isFinished()) {
			++counter;
			int[] playCombination = combinator.makeNext();
			int[] matching = MatchingCalculator.countMatches(playCombination, histCombinations);
			if (isCompatibleToMask(matching, matchingMask)) {
				String complition = Serv.normDoubleX((100.0 * counter / combinations), 2);
				pool.addRecord(playCombination, matching, complition);
				if (display) {
					System.out.println(pool.report(pool.size() - 1));
				}
			}
		}
		return pool;
	}

	private boolean isCompatibleToMask(int[] matching, int[] mask) {
		int matchingIndex = matching.length - 1;
		int maskIndex = mask.length - 1;
		while (matchingIndex >= 0 && maskIndex >= 0) {
			if (matching[matchingIndex] > mask[maskIndex]) {
				return false;
			}
			--matchingIndex;
			--maskIndex;
		}
		return true;
	}

}
