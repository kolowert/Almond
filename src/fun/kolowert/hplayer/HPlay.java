package fun.kolowert.hplayer;

import java.util.Arrays;

public class HPlay {

	public static void main(String[] args) {

//		int len = 8;

		String[] texts = { "a", "b", "c", "abc", "bcd", "premises", "abra kadabra boom", "abra kadAbra boom",
				"aaaaaaaa", "aaaaaaab", "Francesca", "Francesco", "00000000", "London", "Montreal", "Austin Texax US",
				"London is the capital and largest city of England and the United Kingdom. It stands on the River Thames in south-east England at the head of a 50-mile (80 km) estuary down to the North Sea, and has been a major settlement for two millennia.",
				"Lviv", "Yavoriv", "Cieszyn", "Berlin", "Munich", "urvouwhvou", "uvouqeihfih", "UTRUYGLHO",
				"WYUOPYRS", "Rozbahatity" };

//		for (String t : texts) {
//			System.out.println(t + " >> " + HashCoder.makeLettered(t, len) + " >> " + HashCoder.makeInteger(t));
//		}

		for (String t : texts) {
			System.out.println(Arrays.toString(GameSetter.makeGameSet(5, 45, t)));
		}

	}

}
