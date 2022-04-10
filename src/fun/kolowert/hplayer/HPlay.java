package fun.kolowert.hplayer;

import java.util.Arrays;

public class HPlay {

	public static void main(String[] args) {

		boolean letHasher = false;

		hasher(letHasher);

		boolean let5 = true;
		boolean let6 = false;
		boolean letKeno7 = false;

		String[] texts = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "grammar" };

		play(texts, let5, let6, letKeno7);

	}

	private static void play(String[] texts, boolean let5, boolean let6, boolean letKeno7) {
		for (String t : texts) {
			if (let5) {
				String comb = Arrays.toString(GameSetter.makeGameSet(5, 45, t));
				System.out.println("5/45" + "\t" + comb + spaceTip(comb, 5) + "\t" + t);
			}
			if (let6) {
				String comb = Arrays.toString(GameSetter.makeGameSet(6, 52, t));
				System.out.println("6/52" + "\t" + comb + spaceTip(comb, 6) + "\t" + t);
			}
			if (letKeno7) {
				String comb = Arrays.toString(GameSetter.makeGameSet(7, 80, t));
				System.out.println("7/80" + "\t" + comb + spaceTip(comb, 7) + "\t" + t);
			}
		}
	}

	private static String spaceTip(String text, int lenCoef) {
		StringBuilder sb = new StringBuilder();
		int preLen = 4 * lenCoef + 2;
		int len = preLen - text.length();
		for (int i = len; i > 0; i--) {
			sb.append(" ");
		}
		return sb.toString();
	}

	private static void hasher(boolean letHasher) {

		if (!letHasher)
			return;

		int len = 8;

		String[] texts = { "a", "b", "c", "abc", "bcd", "premises", "abra kadabra boom", "abra kadAbra boom",
				"aaaaaaaa", "aaaaaaab", "Francesca", "Francesco", "00000000", "London", "Montreal", "Austin Texax US",
				"London is the capital and largest city of England and the United Kingdom. It stands on the River Thames in south-east England at the head of a 50-mile (80 km) estuary down to the North Sea, and has been a major settlement for two millennia.",
				"Lviv", "Yavoriv", "Cieszyn", "Berlin", "Munich", "urvouwhvou", "uvouqeihfih", "UTRUYGLHO",
				"WYUOPYRS", "Rozbahatity", "Lila", "Rich", "Rich Lila", "Happy Olga",
				"We are the champions my friends", "We are the champions my friends!", "japanese threesome" };

		for (String t : texts) {
			System.out.println(t + " >> " + HashCoder.makeLettered(t, len) + " >> " + HashCoder.makeInteger(t));
		}
	}

}
