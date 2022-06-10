package fun.kolowert.hplayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReporter;
import fun.kolowert.common.HistHandler;
import fun.kolowert.common.MatchingReport;
import fun.kolowert.serv.Sounder;
import fun.kolowert.serv.Timer;

public class HPlay {

	private static final int COMB_SIZE = 8;
	private static final int HIST_DEEP = 100;
	private static final int HIST_SHIFT = 0;

	private static final GameType GAME_TYPE = GameType.SUPER;
	private static final CoderType CODER_TYPE = CoderType.KOLO95;
	
	private static List<int[]> histCombinations = new HistHandler(GAME_TYPE, 2, HIST_SHIFT).getHistCombinations();
	private static String preLastComb = Arrays.toString(histCombinations.get(1)).replaceAll("\\[|\\]|,", "");
	private static String theLastComb = Arrays.toString(histCombinations.get(0)).replaceAll("\\[|\\]|,", "");
	
	private static final String[] SEEDS = { "Get to the United States of America.", 
			"Garage, Implants, New Car, New Computer, Motorcycle, Motor Scooter, Electric Bicycle, Traveling in Europe "
			+ "And other pleasures for myself and some and some relatives", 
			preLastComb, theLastComb };

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
		
		System.out.println(selfReport());
	}

	public static void main(String[] args) {
		HPlay hPlay = new HPlay();
		Timer timer = new Timer();

		hPlay.play(true);

		hPlay.playBook(false);

		hPlay.buncher(false);

		System.out.println("\n~~~ FINISH ~~~");
		System.out.println(timer.reportExtended());
		Sounder.beep();
	}

	public void play(boolean doit) {
		if (!doit)
			return;

		MatchingReporter matchingReporter = new MatchingReporter(GAME_TYPE, HIST_DEEP, HIST_SHIFT);
		int counter = 0;
		for (String seed : SEEDS) {
			int[] playCombination = gameSetter.makeGameSet(combSetSize, gameSetSize, seed);
			MatchingReport matchingReport = matchingReporter.makeMatchingReport(playCombination, seed);
			String pn = ++counter < 10 ? "0" + counter : "" + counter;
			String labe = pn + "  " + combSetSize + "/" + gameSetSize;
			System.out.println(
					labe + "\t" + matchingReport.report());
		}
	}

	/**
	 * it plays with set of words from book
	 */
	public void playBook(boolean doit) {
		if (!doit)
			return;

		System.out.println("\n~~~ playBook ~~~");
		BookDestructor bookDestructor = new BookDestructor1("book.txt");
		List<String> wordSet = bookDestructor.destructToWordSet(false);

		List<String> textUnits = new ArrayList<>();
		for (int i = 0; i < wordSet.size() - 2; i++) {
			textUnits.add(wordSet.get(i + 0) + " " + wordSet.get(i + 1));
			textUnits.add(wordSet.get(i + 0) + " " + wordSet.get(i + 2));
			textUnits.add(wordSet.get(i + 2) + " " + wordSet.get(i + 0));
			textUnits.add(wordSet.get(i + 1) + " " + wordSet.get(i + 0));

			textUnits.add(wordSet.get(i + 0) + " " + wordSet.get(i + 1) + " " + wordSet.get(i + 2));
			textUnits.add(wordSet.get(i + 1) + " " + wordSet.get(i + 2) + " " + wordSet.get(i + 0));
			textUnits.add(wordSet.get(i + 2) + " " + wordSet.get(i + 0) + " " + wordSet.get(i + 1));
		}

		System.out.println("quantity of textUnits is " + textUnits.size() + " hist deep is " + HIST_DEEP);

		List<MatchingReport> matchingReports = analyzeMatching(textUnits, true);

		displayReports(matchingReports);

		String frequencyReport = makeFrequencyReport(matchingReports);
		System.out.println("Frequency of balls in matchingReports: frequency.ball");
		System.out.println(frequencyReport);
	}

	private String makeFrequencyReport(List<MatchingReport> matchingReports) {
		// Count Frequency of balls in matchingReports
		int[] counter = new int[GAME_TYPE.getGameSetSize() + 1];
		for (MatchingReport report : matchingReports) {
			for (int ball : report.getPlayCombination()) {
				++counter[ball];
			}
		}
		// prepare report
		double[] freqReport = new double[GAME_TYPE.getGameSetSize() + 1];
		for (int i = 1; i < counter.length; i++) {
			freqReport[i] = counter[i] + 0.01 * i;
		}
		Arrays.sort(freqReport);
		
		StringBuilder sb = new StringBuilder();
		for (int i = freqReport.length - 1; i >= 0; i--) {
			if (freqReport[i] > 1.0) {
				sb.append(freqReport[i]).append(" ");
			}
		}
		return sb.toString();
	}

	private List<MatchingReport> analyzeMatching(List<String> textUnits, boolean letCollectOnlyBest) {
		List<MatchingReport> bestReports = new ArrayList<>();
		MatchingReporter matchingReporter = new MatchingReporter(GAME_TYPE, HIST_DEEP, HIST_SHIFT);

		for (String textUnit : textUnits) {
			int[] playCombination = gameSetter.makeGameSet(combSetSize, gameSetSize, textUnit);
			MatchingReport analizReport = matchingReporter.makeMatchingReport(playCombination, textUnit);
			int[] matching = analizReport.getMatching();
			if (!letCollectOnlyBest || 
					(
					   matching[matching.length - 1] <= 0 
					&& matching[matching.length - 2] <= 0
					&& matching[matching.length - 3] <= 1 
					&& matching[matching.length - 4] <= 1
					&& matching[matching.length - 5] <= 99
					)
				) 
			{
				bestReports.add(analizReport);
			}
		}
		return bestReports;
	}

	private void displayReports(List<MatchingReport> reports) {
		int counter = 0;
		for (MatchingReport report : reports) {
			String pn = ++counter < 10 ? "00" + counter : counter < 100 ? "0" + counter : "" + counter;
			String labe = "  " + combSetSize + "/" + gameSetSize;
			System.out.println(pn + labe + "\t" + report.report());
		}
	}
	
	private String selfReport() {
		return "COMB_SIZE:" + COMB_SIZE + ", HIST_DEEP:" + HIST_DEEP + ", HIST_SHIFT:" + HIST_SHIFT 
				+ ", GAME_TYPE:" + GAME_TYPE + ", CODER_TYPE:" + CODER_TYPE
				+ " --- " + LocalDate.now() + " " + LocalTime.now();
	}

	/**
	 * It plays bunch of texts
	 */
	private void buncher(boolean letHasher) {
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
				"Ford Fusion +", "Ford Fusion", "Renault Grand Modus", "A car worth up to $6 thousand",
				"A car worth up to $6 thousand, with: gasoline engine, manual gearbox, conditioner, 4 or 5 doors; "
						+ "up to 150k km mileage, not damaged, no accidents, located in western Ukraine, not older "
						+ "than 2008 year",
				"A car worth up to $9 thousand, with: gasoline engine, manual gearbox, conditioner, 4 or 5 doors; "
						+ "up to 150k km mileage, not damaged, no accidents, located in western Ukraine, not older "
						+ "than 2008 year" };

		String[] cTexts = new String[] { "Bay, Boat, Car, House, Airplane, Saile Ship",
				"Bay Boat Car House Airplane Saile Ship", "Fuck the System", "Crack the System",
				"The quick brown fox jumps over the lazy dog", 
				"ThEqUiCkBrOwNfxJuMpSoVeRtHLaZyDGQIcKbWnFXjmPsvlAzYdg 0123456789!",
				"!$0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" };
		
		String[] dText = new String[] { "Garage", "Opel Meriva", "Renault Grand Modus", "Ford Fusion +", 
				"Skoda Roomster", "Skoda Yeti", "Na variata", "Navariata", "На вар'ята",
				"A car worth up to $9 thousand, with: gasoline engine, manual gearbox, conditioner, 4 or 5 doors; "
						+ "up to 150k km mileage, not damaged, no accidents, located in western Ukraine, not older "
						+ "than 2008 year", "Crack the System" };

		System.out.println("\n----hasher");
		System.out.println("type\t< combination >  [ analyzis ]  seeding text unit");
		List<String> allText = prepareTextsList(aTexts, bTexts, cTexts, dText);

		List<MatchingReport> matchingReports = analyzeMatching(allText, false);

		displayReports(matchingReports);
	}

	/**
	 * Servant for buncher
	 */
	private List<String> prepareTextsList(String[]... arrays) {
		List<String> output = new ArrayList<>();
		for (String[] array : arrays) {
			Collections.addAll(output, array);
		}
		return output;
	}
}
