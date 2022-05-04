package fun.kolowert.hplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HPlay {

	private static final int COMB_SIZE = 8;
	private static final int HIST_DEEP = 100;
	private static final GameType GAME_TYPE = GameType.KENO;
	private static final CoderType CODER_TYPE = CoderType.SHA265;
	private static final String[] SEEDS = { "Opel Meriva", "Ford Fusion +", "Skoda Roomster", "Skoda Yeti",
			"A car worth up to $9 thousand, with: gasoline engine, manual gearbox, conditioner, 4 or 5 doors; "
					+ "up to 150k km mileage, not damaged, no accidents, located in western Ukraine, not older "
					+ "than 2008 year",
			"Garage" };

	private int combSetSize;
	private int gameSetSize;

	private GameSetter gameSetter;

	public HPlay() {
		gameSetter = new GameSetter(CODER_TYPE);

		if (GAME_TYPE == GameType.KENO && GAME_TYPE.getCombSetSize() > COMB_SIZE) {
			combSetSize = COMB_SIZE;
		} else {
			combSetSize = GAME_TYPE.getCombSetSize();
		}

		gameSetSize = GAME_TYPE.getGameSetSize();
	}

	public static void main(String[] args) {
		HPlay hPlay = new HPlay();
		hPlay.play();
		hPlay.playBook(true);
		hPlay.hasher(false);
	}

	private void play() {
		int counter = 0;
		for (String seed : SEEDS) {
			int[] playCombination = gameSetter.makeGameSet(combSetSize, gameSetSize, seed);
			String matchingReport = new HistAnalizer(GAME_TYPE).reportMatches(playCombination, HIST_DEEP);
			String pn = ++counter < 10 ? "0" + counter : "" + counter;
			String labe = pn + "  " + combSetSize + "/" + gameSetSize;
			System.out.println(
					labe + "\t" + Serv.normalizeArray(playCombination) + "  " + matchingReport + "  " + seed);
		}
	}

	/**
	 * it plays with set of words from book
	 */
	private void playBook(boolean doit) {
		if (!doit)
			return;

		System.out.println("\n~~~ playBook ~~~");
		BookDestructor bookDest = new BookDestructor1("book.txt");
		List<String> wordSet = bookDest.destructToWordSet();
		System.out.println("quantity of words is " + wordSet.size());

		List<String> analizReport = makeAnalizReport(wordSet);

		for (String a : analizReport) {
			System.out.println(a);
		}
	}

	/**
	 * It plays bunch of texts
	 */
	private void hasher(boolean letHasher) {
		if (!letHasher)
			return;

		String[] aTexts = { "a", "b", "c", "abc", "bcd", "premises", "abra kadabra boom", "abra kadAbra boom",
				"aaaaaaaa", "aaaaaaab", "Francesca", "Francesco", "00000000", "London", "Montreal", "Austin Texax US",
				"London is the capital and largest city of England and the United Kingdom. It stands on the River "
						+ "Thames in south-east England at the head of a 50-mile (80 km) estuary down to the North Sea, "
						+ "and has been a major settlement for two millennia.",
				"Lviv", "Yavoriv", "Cieszyn", "Berlin", "Munich", "urvouwhvou", "uvouqeihfih", "UTRUYGLHO",
				"WYUOPYRS", "Rozbahatity", "Lila", "Rich", "Rich Lila", "Happy Olga", "Horny Natalie",
				"We are the champions my friends", "We are the champions my friends!", "japanese threesome", "Monday",
				"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Grammar" };

		String[] bTexts = { "Garage", "Fiat Grande Punto", "Mitsubishi Space Star", "Opel Meriva", "Skoda Roomster",
				"Skoda Roomster S", "Skoda Roomster Greenline", "Skoda Roomster SE", "Skoda Yeti", "Kia Soul",
				"Ford Fusion +", "Ford Fusion", "A car worth up to $6 thousand",
				"A car worth up to $6 thousand, with: gasoline engine, manual gearbox, conditioner, 4 or 5 doors; "
						+ "up to 150k km mileage, not damaged, no accidents, located in western Ukraine, not older "
						+ "than 2008 year" };

		String[] cTexts = new String[] { "Bay, Boat, Car, House, Airplane, Saile Ship",
				"Bay Boat Car House Airplane Saile Ship", "Fuck the System",
				"The quick brown fox jumps over the lazy dog" };

		List<String> allText = prepareTextsList(aTexts, bTexts, cTexts);
		List<String> analizReport = makeAnalizReport(allText);
		System.out.println("\n----hasher");
		System.out.println("type\t< com  bi  na  ti  on  >  [ an  al   y   z   i   s ]  seeding text\n");
		for (String ar : analizReport) {
			System.out.println(ar);
		}
	}

	private List<String> makeAnalizReport(List<String> textLines) {
		List<String> output = new ArrayList<>();
		int counter = 0;
		for (String textLine : textLines) {
			int[] playCombination = gameSetter.makeGameSet(combSetSize, gameSetSize, textLine);
			String matchingReport = new HistAnalizer(GAME_TYPE).reportMatches(playCombination, HIST_DEEP);
			String pn = ++counter < 10 ? "00" + counter : counter < 100 ? "0" + counter : "" + counter;
			String labe = "  " + combSetSize + "/" + gameSetSize;
			output.add(pn + labe + "\t" + Serv.normalizeArray(playCombination) + "  " + matchingReport + "  "
					+ textLine);
		}
		return output;
	}

	private List<String> prepareTextsList(String[]... arrays) {
		List<String> output = new ArrayList<>();
		for (String[] array : arrays) {
			Collections.addAll(output, array);
		}
		return output;
	}
}
