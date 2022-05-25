package fun.kolowert.bplayer;

public class ResultSet {

	private final int indexHistShift;
	private final double[] frequencyReport;
	private final double[] isolatedHitReport;
	private final long timePoint;
	
	public ResultSet(int indexHistShift, double[] frequencyReport, double[] isolatedHitReport) {
		this.indexHistShift = indexHistShift;
		this.frequencyReport = frequencyReport;
		this.isolatedHitReport = isolatedHitReport;
		this.timePoint = System.currentTimeMillis();
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

	public long getTimePoint() {
		return timePoint;
	}

}
