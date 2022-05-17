package fun.kolowert.cplayer;

import java.util.Objects;

public class FreqReportForCouple implements Comparable<FreqReportForCouple> {

	private final int aBall;
	private final int bBall;
	private int frequency = 0;

	public FreqReportForCouple(int a, int b) {
		aBall = a;
		bBall = b;
	}
	
	public FreqReportForCouple(int[] c) {
		aBall = c[0];
		bBall = c[1];
	}

	public void increaseFrequency() {
		++frequency;
	}

	public int[] getBallCouple() {
		return new int[] { aBall, bBall };
	}

	public int getFrequency() {
		return frequency;
	}

	@Override
	public int hashCode() {
		return Objects.hash(aBall, bBall);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FreqReportForCouple other = (FreqReportForCouple) obj;
		return aBall == other.aBall && bBall == other.bBall;
	}

	@Override
	public int compareTo(FreqReportForCouple other) {
		return this.frequency - other.getFrequency();
	}
	
	
}
