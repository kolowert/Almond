package fun.kolowert.hplayer;

import java.util.Arrays;

public class HPlay {

	public static void main(String[] args) {

		boolean letHasher = false;

		hasher(letHasher);

		boolean let5 = true;
		boolean let6 = false;

		String[] texts = { "Inadventent", "Diligence" };

		play(texts, let5, let6);

	}

	private static void play(String[] texts, boolean let5, boolean let6) {
		for (String t : texts) {
			if (let5)
				System.out.println(t + "\t" + "5/45" + "\t" + Arrays.toString(GameSetter.makeGameSet(5, 45, t)));
			if (let6)
				System.out.println(t + "\t" + "6/52" + "\t" + Arrays.toString(GameSetter.makeGameSet(6, 52, t)));
		}
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
