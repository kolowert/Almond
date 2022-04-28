package fun.kolowert.hplayer;

import java.util.Arrays;

public class HPlay {

	private static final GameType GAMETYPE = GameType.KENO;
	private static final CoderType CODERTYPE = CoderType.SHA265;
	private static final String[] SEEDS = { "Ford Fusion +", "Skoda Roomster" };
	
	private int combSetSize;
	private int playSetSize;
	private int gameSetSize;

	private GameSetter gameSetter;

	public HPlay() {
		gameSetter = new GameSetter(CODERTYPE);
		combSetSize = GAMETYPE.getCombSetSize();
		playSetSize = GAMETYPE.getPlaySetSize();
		gameSetSize = GAMETYPE.getGameSetSize();
	}

	public static void main(String[] args) {

		HPlay hPlay = new HPlay();

		hPlay.play();

		hPlay.hasher(true);

	}

	private void play() {

		for (String seed : SEEDS) {

			String comb = Arrays.toString(gameSetter.makeGameSet(combSetSize, gameSetSize, seed));
			String labe = "" + playSetSize + "/" + gameSetSize;
			System.out.println(labe + "\t" + comb + spaceTip(comb, combSetSize) + "\t" + seed);

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

	private void hasher(boolean letHasher) {

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
				int[] playCombination = gameSetter.makeGameSet(combSetSize, gameSetSize, t);
				String matchingReport = new HistAnalizer(GAMETYPE).reportMatches(playCombination);
				String labe = "" + combSetSize + "/" + gameSetSize;
				System.out.println(
						labe + "\t" + Serv.normalizeArray(playCombination) + "  " + matchingReport + "  " + t);
			}
		}

	}

}
