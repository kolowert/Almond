package fun.kolowert.bplayer;

public class ResultSet implements Comparable<ResultSet> {

	private final int indexHistShift;
	private final double[] frequencyReport;
	private final double[] isolatedHitReport; // TODO  rid of it
	private final int poolSize;
	private final long timePoint;

	public ResultSet(int indexHistShift, double[] frequencyReport, double[] isolatedHitReport, int poolSize) {
		this.indexHistShift = indexHistShift;
		this.frequencyReport = frequencyReport;
		this.isolatedHitReport = isolatedHitReport;
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

	public double[] getIsolatedHitReport() {
		return isolatedHitReport;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public long getTimePoint() {
		return timePoint;
	}

}
