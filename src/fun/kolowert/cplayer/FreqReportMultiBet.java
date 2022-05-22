package fun.kolowert.cplayer;

import java.util.Arrays;

import fun.kolowert.serv.Serv;

public class FreqReportMultiBet implements Comparable<FreqReportMultiBet> {

	private final int[] multiBet;
	private int frequency = 0;
	
	public FreqReportMultiBet(int[] bet) {
		multiBet = bet;
	}
	
	public void increaseFrequency() {
		++frequency;
	}

	public int[] getMultyBet() {
		return multiBet;
	}

	public int getFrequency() {
		return frequency;
	}

	@Override
	public int compareTo(FreqReportMultiBet other) {
		return this.frequency - other.getFrequency();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(multiBet);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FreqReportMultiBet other = (FreqReportMultiBet) obj;
		return Arrays.equals(multiBet, other.multiBet);
	}

	@Override
	public String toString() {
		return "FreqReportMultiBet [multiBet=" + Arrays.toString(multiBet) + ", frequency=" + frequency + "]";
	}
	
	public String report() {
		StringBuilder sb = new StringBuilder();
		sb.append(Serv.normIntX(frequency, 3, "0")).append("(");
		for (int b : multiBet) {
			sb.append(Serv.normIntX(b, 2, "0")).append(" ");
		}
		String result = sb.toString();
		return result.substring(0, result.length() - 1) + ")";
	}
}
