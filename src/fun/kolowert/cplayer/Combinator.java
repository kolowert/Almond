package fun.kolowert.cplayer;

import java.util.Arrays;

/**
 * Once created this by next() returns next combination from 1, 2, 3, ... n to
 * n-m, n-m+1, ... n-1, n After last combination this return null ! !!! it works
 * only if combinationSize <= 8 (Must be improved)
 */
public class Combinator {

	private final int fullSet;
	private final int combinationSize;
	private int[] combination;
	private int[] lastCombination;
	private boolean isFirst;
	private boolean isFinished;

	public Combinator(int combinationSize, int fullSet) {
		this.fullSet = fullSet;
		this.combinationSize = combinationSize;
		check();
		isFirst = true;
		combination = makeFirst();
		lastCombination = makeLast();
		isFinished = false;
	}

	private void check() {
		if (combinationSize >= fullSet) {
			throw new IllegalArgumentException("fullSet should be biger than combinationSize");
		}
		if (combinationSize > 10) {
			throw new IllegalArgumentException("combinationSize should be not biger than 10");
		}
	}

	private int[] makeFirst() {
		int[] result = new int[combinationSize];
		int counter = 0;
		for (int i = 0; i < combinationSize; i++) {
			result[i] = ++counter;
		}
		return result;
	}

	private int[] makeLast() {
		int[] result = new int[combinationSize];
		int counter = fullSet - combinationSize;
		for (int i = 0; i < combinationSize; i++) {
			result[i] = ++counter;
		}
		return result;
	}

	public int[] makeNext() {
		if (isFirst) {
			isFirst = false;
			return combination;
		}

		int poz = combinationSize - 1;
		if (combination[poz] < fullSet) {
			++combination[poz];
		} else if (combination[poz - 1] < fullSet - 1) {
			++combination[poz - 1];
			combination[poz] = combination[poz - 1] + 1;
		} else if (combination[poz - 2] < fullSet - 2) {
			++combination[poz - 2];
			combination[poz - 1] = combination[poz - 2] + 1;
			combination[poz] = combination[poz - 1] + 1;
		} else if (combination[poz - 3] < fullSet - 3) {
			++combination[poz - 3];
			combination[poz - 2] = combination[poz - 3] + 1;
			combination[poz - 1] = combination[poz - 2] + 1;
			combination[poz] = combination[poz - 1] + 1;
		} else if (combination[poz - 4] < fullSet - 4) {
			++combination[poz - 4];
			combination[poz - 3] = combination[poz - 4] + 1;
			combination[poz - 2] = combination[poz - 3] + 1;
			combination[poz - 1] = combination[poz - 2] + 1;
			combination[poz] = combination[poz - 1] + 1;
		} else if (combination[poz - 5] < fullSet - 5) {
			++combination[poz - 5];
			combination[poz - 4] = combination[poz - 5] + 1;
			combination[poz - 3] = combination[poz - 4] + 1;
			combination[poz - 2] = combination[poz - 3] + 1;
			combination[poz - 1] = combination[poz - 2] + 1;
			combination[poz] = combination[poz - 1] + 1;
		} else if (combination[poz - 6] < fullSet - 6) {
			++combination[poz - 6];
			combination[poz - 5] = combination[poz - 6] + 1;
			combination[poz - 4] = combination[poz - 5] + 1;
			combination[poz - 3] = combination[poz - 4] + 1;
			combination[poz - 2] = combination[poz - 3] + 1;
			combination[poz - 1] = combination[poz - 2] + 1;
			combination[poz] = combination[poz - 1] + 1;
		} else if (combination[poz - 7] < fullSet - 7) {
			++combination[poz - 7];
			combination[poz - 6] = combination[poz - 7] + 1;
			combination[poz - 5] = combination[poz - 6] + 1;
			combination[poz - 4] = combination[poz - 5] + 1;
			combination[poz - 3] = combination[poz - 4] + 1;
			combination[poz - 2] = combination[poz - 3] + 1;
			combination[poz - 1] = combination[poz - 2] + 1;
			combination[poz] = combination[poz - 1] + 1;
		} else if (combination[poz - 8] < fullSet - 8) {
			++combination[poz - 8];
			combination[poz - 7] = combination[poz - 8] + 1;
			combination[poz - 6] = combination[poz - 7] + 1;
			combination[poz - 5] = combination[poz - 6] + 1;
			combination[poz - 4] = combination[poz - 5] + 1;
			combination[poz - 3] = combination[poz - 4] + 1;
			combination[poz - 2] = combination[poz - 3] + 1;
			combination[poz - 1] = combination[poz - 2] + 1;
			combination[poz] = combination[poz - 1] + 1;
		} else if (combination[poz - 9] < fullSet - 9) {
			++combination[poz - 9];
			combination[poz - 8] = combination[poz - 9] + 1;
			combination[poz - 7] = combination[poz - 8] + 1;
			combination[poz - 6] = combination[poz - 7] + 1;
			combination[poz - 5] = combination[poz - 6] + 1;
			combination[poz - 4] = combination[poz - 5] + 1;
			combination[poz - 3] = combination[poz - 4] + 1;
			combination[poz - 2] = combination[poz - 3] + 1;
			combination[poz - 1] = combination[poz - 2] + 1;
			combination[poz] = combination[poz - 1] + 1;
		}

		if (combination[0] == lastCombination[0]) {
			isFinished = true;
		}

		int[] result = new int[combinationSize];
		System.arraycopy(combination, 0, result, 0, combinationSize);
		return result;
	}

	public boolean isFinished() {
		return isFinished;
	}
	
	public boolean hasNext() {
		return !isFinished;
	}
	
	public static String reportCombinationsQuantity(int subSet, int superSet) {
		long combinationsQuantity = calculateCombinations(subSet, superSet);
		return String.format("%d from %d = %,d combinations%n", subSet, superSet, combinationsQuantity);
	}
	
	public static long calculateCombinations(int subSet, int superSet) {
		int dif = superSet - subSet;
		long subSetFactorial = factorial(subSet);
		long firstProduct = superSet * (superSet - 1);
		long nextProduct = 1;
		for (int i = superSet - 2; i > dif; i--) {
			nextProduct *= i;
		}
		double preResult = (double) firstProduct / subSetFactorial;
		long result = (long) (preResult * nextProduct);
		return result;
	}

	private static long factorial(int n) {
		long fact = 1;
		for (int i = 2; i <= n; i++) {
			fact = fact * i;
		}
		return fact;
	}

	// debug
	public static void main(String[] args) {
		int subSet = 3;
		int superSet = 6;
		Combinator.calculateCombinations(subSet, superSet);
		Combinator combinator = new Combinator(subSet, superSet);
		int counter = 0;
		while (!combinator.isFinished) {
			++counter;
			int[] combination = combinator.makeNext();
			System.out.println(counter + " " + Arrays.toString(combination));
		}

	}
}
