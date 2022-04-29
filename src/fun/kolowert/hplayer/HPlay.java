package fun.kolowert.hplayer;

public class HPlay {

	private static final int COMB_SIZE = 6;
	private static final int HIST_DEEP = 30;
	private static final GameType GAME_TYPE = GameType.MAXI;
	private static final CoderType CODER_TYPE = CoderType.SHA265;
	private static final String[] SEEDS = { "Opel Meriva", "Ford Fusion +", "Skoda Roomster", "Fiat Grande Punto",
			"A car worth up to $6 thousand, with: gasoline engine, manual gearbox, conditioner, 4 or 5 doors; "
					+ "up to 150k km mileage, not damaged, no accidents, located in western Ukraine, not older than 2008 year" };

	private int combSetSize;
	private int playSetSize;
	private int gameSetSize;

	private GameSetter gameSetter;

	public HPlay() {
		gameSetter = new GameSetter(CODER_TYPE);

		if (GAME_TYPE == GameType.KENO && GAME_TYPE.getCombSetSize() > COMB_SIZE) {
			combSetSize = COMB_SIZE;
		} else {
			combSetSize = GAME_TYPE.getCombSetSize();
		}

		playSetSize = GAME_TYPE.getPlaySetSize();
		gameSetSize = GAME_TYPE.getGameSetSize();
	}

	public static void main(String[] args) {
		HPlay hPlay = new HPlay();
		hPlay.play();
		hPlay.hasher(true);
	}

	private void play() {
		for (String seed : SEEDS) {
			String comb = Serv.normalizeArray(gameSetter.makeGameSet(combSetSize, gameSetSize, seed), "<", ">");
			String labe = "" + playSetSize + "/" + gameSetSize;
			System.out.println(labe + "\t" + comb + "\t" + seed);
		}
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

		String[] bTexts = { "Garage", "Fiat Grande Punto", "Mitsubishi Space Star", "Opel Meriva", "Skoda Roomster",
				"Skoda Roomster S", "Skoda Roomster Greenline", "Skoda Roomster SE", "Skoda Yeti", "Kia Soul",
				"Ford Fusion +", "Ford Fusion", "A car worth up to $6 thousand",
				"A car worth up to $6 thousand, with: gasoline engine, manual gearbox, conditioner, 4 or 5 doors; "
						+ "up to 150k km mileage, not damaged, no accidents, located in western Ukraine, not older than 2008 year" };

		Object[] allTexts = { aTexts, bTexts,
				new String[] { "Bay, Boat, Car, House, Airplane, Saile Ship",
						"Bay Boat Car House Airplane Saile Ship", "Fuck the System",
						"The quick brown fox jumps over the lazy dog" } };

		System.out.println("\n----");
		System.out.println("type\t< com  bi  na  ti  on  >  [ an  al   y   z   i   s ]  seeding text\n");
		int counter = 0;
		for (Object s : allTexts) {
			for (String t : (String[]) s) {
				int[] playCombination = gameSetter.makeGameSet(combSetSize, gameSetSize, t);
				String matchingReport = new HistAnalizer(GAME_TYPE).reportMatches(playCombination, HIST_DEEP);
				String pn = ++counter < 10 ? "0" + counter : "" + counter;
				String labe = "  " + combSetSize + "/" + gameSetSize;
				System.out.println(
						pn + labe + "\t" + Serv.normalizeArray(playCombination) + "  " + matchingReport + "  " + t);
			}
		}

	}

}
