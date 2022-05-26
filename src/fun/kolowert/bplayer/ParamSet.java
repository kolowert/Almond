package fun.kolowert.bplayer;

import java.util.Arrays;

import fun.kolowert.common.GameType;

public class ParamSet {

	private final GameType gameType;
	private final int playSet;
	private final int histDeep;
	private final int histShift;
	private final int histShifts;
	private final int[] matchingMask;
	private final int[] hitMaskIsolated;
	private long timePoint;

	public ParamSet(GameType gameType, int playSet, int histDeep, int histShift, int histShifts, int[] matchingMask,
			int[] hitMaskIsolated) {
		this.gameType = gameType;
		this.playSet = playSet;
		this.histDeep = histDeep;
		this.histShift = histShift;
		this.histShifts = histShifts;
		this.matchingMask = matchingMask;
		this.hitMaskIsolated = hitMaskIsolated;
		timePoint = System.currentTimeMillis();
	}

	public long getTimePoint() {
		return timePoint;
	}

	public void setTimePoint(long timePoint) {
		this.timePoint = timePoint;
	}

	public GameType getGameType() {
		return gameType;
	}

	public int getPlaySet() {
		return playSet;
	}

	public int getHistDeep() {
		return histDeep;
	}

	public int getHistShift() {
		return histShift;
	}

	public int getHistShifts() {
		return histShifts;
	}

	public int[] getMatchingMask() {
		return matchingMask;
	}

	public int[] getHitMaskIsolated() {
		return hitMaskIsolated;
	}

	@Override
	public String toString() {
		return "ParamSet [gameType=" + gameType + ", playSet=" + playSet + ", histDeep=" + histDeep + ", histShift="
				+ histShift + ", histShifts=" + histShifts + ", matchingMask=" + Arrays.toString(matchingMask)
				+ ", hitMaskIsolated=" + Arrays.toString(hitMaskIsolated) + ", timePoint=" + timePoint + "]";
	}

}
