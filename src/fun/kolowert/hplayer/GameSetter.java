package fun.kolowert.hplayer;

import java.util.Arrays;

public class GameSetter {

	private Coder coder;

	public GameSetter() {
		coder = new Coder256Sha();
	}

	public GameSetter(CoderType c) {
		switch (c) {
		case KOLO64:
			coder = new CoderKolo64();
			break;
		case KOLO95:
			coder = new CoderKolo95();
			break;	
		case SHA256:
			coder = new Coder256Sha();
			break;
		default:
			coder = new Coder256Sha();
		}
	}

	/**
	 * Creates combination from text unit
	 * 
	 * @param balls    - quantity of balls in play-combination
	 * @param ballSet  - quantity of balls in game
	 * @param textUnit
	 * @return combination generated by parameters
	 */
	public int[] makeGameSet(int balls, int ballSet, String textUnit) {

		int deep = 3;
		int repetition = 2 * ballSet;

		int[] preCombination = new int[balls];
		int[] combination = new int[balls];

		String a = coder.makeLettered(textUnit, deep + deep * balls);

		for (int i = 0; i < balls; i++) {
			for (int j = 0; j < deep; j++) {
				preCombination[i] += a.charAt(i * deep + j);
			}
		}

		for (int i = 0; i < balls; i++) {
			int counter = 0;
			do {
				if (++counter > repetition) {
					break;
				}
				preCombination[i] *= 3;
				combination[i] = 1 + (counter + preCombination[i]) % ballSet;
			} while (checkRepetition(combination));
		}

		Arrays.sort(combination);

		return combination;
	}

	private boolean checkRepetition(int[] combination) {
		for (int i = 1; i < combination.length; i++) {
			for (int j = 0; j < i; j++) {
				if (combination[i] != 0 && combination[i] == combination[j]) {
					return true;
				}
			}
		}
		return false;
	}

	// debugging
	public static void main(String[] args) {
		GameSetter gameSetter = new GameSetter();
		int[] playCombination = gameSetter.makeGameSet(8, 80, "abrupt conformed");
		System.out.println(Arrays.toString(playCombination));
	}

}
