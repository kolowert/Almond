package fun.kolowert.hplayer;

import java.util.Arrays;

public class GameSetter {
	
	private Coder coder;
	
	public GameSetter() {
		coder = new Coder256Sha();
	}
	
	public GameSetter(Coders c) {
		if (c == Coders.KOLO64) {
			coder = new CoderKolo64();
		} else {
			coder = new Coder256Sha();
		}
	}
	
	public class Inner {
		private Inner() {}
		public String reportJob(int balls, int ballSet, String seeds) {
			return Arrays.toString(makeGameSet(balls, ballSet, seeds));
		}
	}
	
	public int[] makeGameSet(int balls, int ballSet, String seeds) {
		
		int deep = 4;
		int repetition = 2 * ballSet;
		
		int[] preCombination = new int[balls];
		int[] combination = new int[balls];
		
		String a = coder.makeLettered(seeds, deep * balls);

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
				combination[i] = counter + preCombination[i] % ballSet;
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
	
}
