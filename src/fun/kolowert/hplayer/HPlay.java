package fun.kolowert.hplayer;

import java.util.Arrays;

public class HPlay {

	public static void main(String[] args) {
		
		// parameters
		boolean let5 = true;
		boolean let6 = false;
		boolean letKeno8 = false;
		GameType gameType = GameType.MAXI;
		GameSetter gameSetter = new GameSetter(Coders.SHA265);

		String[] texts = { "Ford Fusion +", "Ford Fusion", "Skoda Roomster Greenline" };

		play(texts, let5, let6, letKeno8, gameSetter);

		hasher(true, gameSetter, gameType);

	}

	private static void play(String[] texts, boolean let5, boolean let6, boolean letKeno, GameSetter gameSetter) {

		for (String t : texts) {
			if (let5) {
				String comb = Arrays.toString(gameSetter.makeGameSet(5, 45, t));
				System.out.println("5/45" + "\t" + comb + spaceTip(comb, 5) + "\t" + t);
			}
			if (let6) {
				String comb = Arrays.toString(gameSetter.makeGameSet(6, 52, t));
				System.out.println("6/52" + "\t" + comb + spaceTip(comb, 6) + "\t" + t);
			}
			if (letKeno) {
				String comb = Arrays.toString(gameSetter.makeGameSet(8, 80, t));
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

	private static void hasher(boolean letHasher, GameSetter gameSetter, GameType gameType) {

		if (!letHasher)
			return;

		String[] aTexts = { "a", "b", "c", "abc", "bcd", "premises", "abra kadabra boom", "abra kadAbra boom",
				"aaaaaaaa", "aaaaaaab", "Francesca", "Francesco", "00000000", "London", "Montreal", "Austin Texax US",
				"London is the capital and largest city of England and the United Kingdom. It stands on the River "
						+ "Thames in south-east England at the head of a 50-mile (80 km) estuary down to the North Sea, and "
						+ "has been a major settlement for two millennia.",
				"Lviv", "Yavoriv", "Cieszyn", "Berlin", "Munich", "urvouwhvou", "uvouqeihfih", "UTRUYGLHO",
				"WYUOPYRS", "Rozbahatity", "Lila", "Rich", "Rich Lila", "Happy Olga", "Horny Natalie",
				"We are the champions my friends", "We are the champions my friends!", "japanese threesome", "Monday",
				"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Grammar" };

		String[] bTexts = { "Garage", "Fiat Grande Punto", "Mitsubishi Space Star", "Opel Meriva", "Skoda Roomster S",
				"Skoda Roomster Greenline", "Skoda Roomster SE", "Skoda Yeti", "Kia Soul", "Ford Fusion +",
				"Ford Fusion" };

		Object[] allTexts = { aTexts, bTexts,
				new String[] { "Bay, Boat, Car, House, Airplane, Saile Ship",
						"Bay Boat Car House Airplane Saile Ship", "Fuck the System",
						"The quick brown fox jumps over the lazy dog" } };

		System.out.println("\n----");
		System.out.println("type\t< com  bi  na  ti  on  >  [ an  al   y   z   i   s ]  seeding text\n");
		for (Object s : allTexts) {
			for (String t : (String[]) s) {
				int[] gameSet = gameSetter.makeGameSet(5, 45, t);
				String matchingReport = new HistAnalizer(gameType).reportMatches(gameSet);
				System.out.println("5/45" + "\t" + Serv.normalizeArray(gameSet) + "  " + matchingReport + "  " + t);
			}
		}

	}

}
