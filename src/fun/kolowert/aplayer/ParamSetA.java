package fun.kolowert.aplayer;

import fun.kolowert.common.GameType;

public class ParamSetA {
	
	private final GameType gameType;
	private final int playSet;
	private final int histDeep;
	private final int histShift;
	private final int reportLimit;
	
	public ParamSetA(GameType gameType, int playSet, int histDeep, int histShift, int reportLimit) {
		this.gameType = gameType;
		this.playSet = playSet;
		this.histDeep = histDeep;
		this.histShift = histShift;
		this.reportLimit = reportLimit;
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

	public int getReportLimit() {
		return reportLimit;
	}
	
	
}
