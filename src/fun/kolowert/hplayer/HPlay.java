package fun.kolowert.hplayer;

import java.util.Arrays;

public class HPlay {

	public static void main(String[] args) {

		boolean letHasher = false;

		hasher(letHasher);

		boolean let5 = true;
		boolean let6 = false;
		boolean letKeno8 = false;

		String[] texts = { "Hyundai i10", "Renault Grand Modus", "Fiat Grande Punto" };

		play(texts, let5, let6, letKeno8);

		experimental(texts, true);

	}

	private static void experimental(String[] texts, boolean doIt) {
		if (doIt) {
			Runtime runtime = Runtime.getRuntime();
			System.out.println("\nruntime: " + runtime);
			System.out.println("availableProcessors: " + runtime.availableProcessors());

			System.out.println("\nexperimental");
			System.out.println(texts.toString());
			System.out.println(texts.getClass());

			String abc = "qwe";
			System.out.println(abc);
			System.out.println(abc.toString());
			System.out.println(abc.getClass());

			System.out.println(texts instanceof String[]);

			String reportedJob = GameSetter.Inner.reportJob(5, 45, texts[0]);
			System.out.println(reportedJob);
		}
	}

	private static void play(String[] texts, boolean let5, boolean let6, boolean letKeno) {
		for (String t : texts) {
			if (let5) {
				String comb = Arrays.toString(GameSetter.makeGameSet(5, 45, t));
				System.out.println("5/45" + "\t" + comb + spaceTip(comb, 5) + "\t" + t);
			}
			if (let6) {
				String comb = Arrays.toString(GameSetter.makeGameSet(6, 52, t));
				System.out.println("6/52" + "\t" + comb + spaceTip(comb, 6) + "\t" + t);
			}
			if (letKeno) {
				String comb = Arrays.toString(GameSetter.makeGameSet(8, 80, t));
				System.out.println("8/80" + "\t" + comb + spaceTip(comb, 8) + "\t" + t);
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

		String[] aTexts = { "a", "b", "c", "abc", "bcd", "premises", "abra kadabra boom", "abra kadAbra boom",
				"aaaaaaaa", "aaaaaaab", "Francesca", "Francesco", "00000000", "London", "Montreal", "Austin Texax US",
				"London is the capital and largest city of England and the United Kingdom. It stands on the River Thames in south-east England at the head of a 50-mile (80 km) estuary down to the North Sea, and has been a major settlement for two millennia.",
				"Lviv", "Yavoriv", "Cieszyn", "Berlin", "Munich", "urvouwhvou", "uvouqeihfih", "UTRUYGLHO",
				"WYUOPYRS", "Rozbahatity", "Lila", "Rich", "Rich Lila", "Happy Olga",
				"We are the champions my friends", "We are the champions my friends!", "japanese threesome", "Monday",
				"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "grammar" };

		String[] bTexts = { "Fiat Grande Punto", "Mitsubishi Space Star", "Opel Meriva", "Skoda Roomster",
				"Skoda Yeti", "Kia Soul", "Garage" };

		Object[] allTexts = { aTexts, bTexts, new String[] { "Bay, Boat, Car, House, Airplane, Saile Ship",
				"Bay Boat Car House Airplane Saile Ship" } };

		for (Object s : allTexts) {
			for (String t : (String[]) s) {
				System.out.println(t + " >> " + HashCoder.makeLettered(t, len) + " >> " + HashCoder.makeInteger(t));
			}
		}
	}

}
