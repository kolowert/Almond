package fun.kolowert.aplayer;

import java.util.Objects;

import fun.kolowert.serv.Serv;

public class BigCombination implements Comparable<BigCombination> {

	private final int[] combination;
	private final int[] matching;
	private final int score;

	public BigCombination(int[] combination, int[] matching, int score) {
		this.combination = combination;
		this.matching = matching;
		this.score = score;
	}

	public String report() {
		StringBuilder sb = new StringBuilder(combination.length + 1);
		if (combination.length < 1) {
			return "Zero Lines in Report!";
		}

		sb.append(Serv.normalizeArray(combination)).append("  ").append(Serv.normalizeArray(matching, "[", "]"))
				.append(" ").append(Serv.normIntX(score, 4, " ")).append(" $ ");

		return sb.toString();
	}

	@Override
	public int compareTo(BigCombination o) {
		return score - o.getScore();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(score);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BigCombination other = (BigCombination) obj;
		return score == other.score;
	}

	public int getCombinationLength() {
		return combination.length;
	}
	
	public int getScore() {
		return score;
	}
}
