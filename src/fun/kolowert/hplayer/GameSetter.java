package fun.kolowert.hplayer;

import java.util.Arrays;

public class GameSetter {

	private GameSetter() {
	}

	public static int[] makeGameSet(int balls, int ballSet, String seeds) {
		int[] preCombination = new int[balls];
		int[] combination = new int[balls];
		int deep = 4;

		String a = HashCoder.makeLettered(seeds, deep * balls);

		for (int i = 0; i < balls; i++) {
			for (int j = 0; j < deep; j++) {
				preCombination[i] += a.charAt(i * deep + j);
			}
		}

		for (int i = 0; i < balls; i++) {
			int counter = 0;
			do {
				if (++counter > 5) {
					break;
				}
				preCombination[i] *= 3;
				combination[i] = 1 + preCombination[i] % ballSet;
			} while (checkRepetition(combination));
		}

		Arrays.sort(combination);

		return combination;
	}

	private static boolean checkRepetition(int[] combination) {
		for (int i = 1; i < combination.length; i++) {
			for (int j = 0; j < i; j++) {
				if (combination[i] != 0 && combination[i] == combination[j]) {
					return true;
				}
			}
		}
		return false;
	}

}
