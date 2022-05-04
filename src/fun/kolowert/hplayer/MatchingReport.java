package fun.kolowert.hplayer;

import java.util.Arrays;

public class MatchingReport {

	private int[] playCombination;
	private int[] matching;
	private String textUnit;

	public MatchingReport(int[] playCombination, int[] matching, String textUnit) {
		super();
		this.playCombination = playCombination;
		this.matching = matching;
		this.textUnit = textUnit;
	}
	
	public String report() {
		return Serv.normalizeArray(playCombination) + "  " + Serv.normalizeArray(matching, "[", "]") + "  " + textUnit;
	}
	
	public int[] getMatching() {
		return matching;
	}
	
	@Override
	public String toString() {
		return "MatchingReport [playCombination=" + Arrays.toString(playCombination) + ", matchin="
				+ Arrays.toString(matching) + ", textUnit=" + textUnit + "]";
	}

}
