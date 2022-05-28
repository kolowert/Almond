package fun.kolowert.bplayer;

import java.util.List;

import fun.kolowert.common.FreqReporterOnPool;
import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReportPool;
import fun.kolowert.cplayer.PoolPlay;

public class ResultMaker implements Runnable {

	private final int indexHistShift;

	private final GameType gameType;
	private final int playSet;
	private final int histDeep;
	private final int[] matchingMask;
	
	private ResultSet resultSet;
	private boolean isResultSetMade = false;
	private List<ResultSet> results;
	
	public ResultMaker(ParamSet paramSet, int indexHistShift, List<ResultSet> results) {
		gameType = paramSet.getGameType();
		playSet = paramSet.getPlaySet();
		histDeep = paramSet.getHistDeep();
		matchingMask = paramSet.getMatchingMask();
		this.indexHistShift = indexHistShift;
		this.results = results;
	}

	@Override
	public void run() {
		resultSet = makeResult();
		isResultSetMade = true;
		results.add(resultSet);
	}
	
	public ResultSet getResultSet() {
		if (!isResultSetMade) {
			resultSet = makeResult();
			isResultSetMade = true;
		}
		return resultSet;
	}
	
	public ResultSet makeResult() {

		PoolPlay poolPlay = new PoolPlay(gameType, playSet, matchingMask);
		MatchingReportPool pool = poolPlay.makeMatchingReportPool(histDeep, indexHistShift, false);

		FreqReporterOnPool freqReporter = new FreqReporterOnPool(gameType, pool);
		double[] frequencyReport = freqReporter.getFrequencyReport();

		return new ResultSet(indexHistShift, frequencyReport, pool.size());
	}

}
