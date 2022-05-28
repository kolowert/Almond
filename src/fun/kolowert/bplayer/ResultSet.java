package fun.kolowert.bplayer;

public class ResultSet implements Comparable<ResultSet> {

	private final int indexHistShift;
	private final double[] frequencyReport;
	private final int poolSize;
	private final long timePoint;

	public ResultSet(int indexHistShift, double[] frequencyReport, int poolSize) {
		this.indexHistShift = indexHistShift;
		this.frequencyReport = frequencyReport;
		this.poolSize = poolSize;
		timePoint = System.currentTimeMillis();
	}

	@Override
	public int compareTo(ResultSet o) {
		return o.getIndexHistShift() - indexHistShift;
	}

	public int getIndexHistShift() {
		return indexHistShift;
	}

	public double[] getFrequencyReport() {
		return frequencyReport;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public long getTimePoint() {
		return timePoint;
	}

}
