package fun.kolowert.hplayer;

import java.util.Arrays;

public class HPlay {

	public static void main(String[] args) {

		boolean letHasher = false;

		hasher(letHasher);

		boolean let5 = true;
		boolean let6 = false;
		boolean letKeno8 = false;

		String[] texts = { "Ford Fusion +", "Ford Fusion" };

		play(texts, let5, let6, letKeno8);

		hasher(true);

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

		String[] aTexts = { "a", "b", "c", "abc", "bcd", "premises", "abra kadabra boom", "abra kadAbra boom",
				"aaaaaaaa", "aaaaaaab", "Francesca", "Francesco", "00000000", "London", "Montreal", "Austin Texax US",
				"London is the capital and largest city of England and the United Kingdom. It stands on the River "
						+ "Thames in south-east England at the head of a 50-mile (80 km) estuary down to the North Sea, and "
						+ "has been a major settlement for two millennia.",
				"Lviv", "Yavoriv", "Cieszyn", "Berlin", "Munich", "urvouwhvou", "uvouqeihfih", "UTRUYGLHO",
				"WYUOPYRS", "Rozbahatity", "Lila", "Rich", "Rich Lila", "Happy Olga",
				"We are the champions my friends", "We are the champions my friends!", "japanese threesome", "Monday",
				"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "grammar" };

		String[] bTexts = { "Fiat Grande Punto", "Mitsubishi Space Star", "Opel Meriva", "Skoda Roomster",
				"Skoda Yeti", "Kia Soul", "Ford Fusion +", "Ford Fusion", "Garage" };

		Object[] allTexts = { aTexts, bTexts, new String[] { "Bay, Boat, Car, House, Airplane, Saile Ship",
				"Bay Boat Car House Airplane Saile Ship" } };
		
		System.out.println("\n----");
		System.out.println("type\t< com  bi  na  ti  on  >  [ an  al   y   z   i   s ]  seeding text\n");
		for (Object s : allTexts) {
			for (String t : (String[]) s) {
				int[] gameSet = GameSetter.makeGameSet(5, 45, t);
				String setReport = new HistAnalizer().reportMatches(gameSet);
				System.out.println("5/45" + "\t" + Serv.normalizeArray(gameSet) + "  " + setReport + "  " + t);
			}
		}

	}

}
