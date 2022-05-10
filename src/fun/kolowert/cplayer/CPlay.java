package fun.kolowert.cplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.HistAnalizer;
import fun.kolowert.common.MatchingReport;
import fun.kolowert.serv.Serv;
import fun.kolowert.serv.Timer;

public class CPlay {

	private GameType gameType;
	private int histShift;

	private int combSetSize;
	private int gameSetSize;

	private int[] matchingMask;

	private HistAnalizer histAnalyzer;

	public CPlay(GameType gameType, int combSize, int histDeep, int histShift, int[] matchingMask) {
		if (gameType == GameType.KENO && gameType.getCombSetSize() > combSize) {
			combSetSize = combSize;
		} else {
			combSetSize = gameType.getCombSetSize();
		}

		this.gameType = gameType;
		this.histShift = histShift;
		this.gameSetSize = gameType.getGameSetSize();
		this.matchingMask = matchingMask;

		histAnalyzer = new HistAnalizer(gameType, histDeep, histShift);
	}

	// debugging
	public static void main(String[] args) {
		CPlay cPlay = new CPlay(GameType.KENO, 4, 24, 3, new int[] { 100, 100, 0, 0, 0 });
		Timer timer = new Timer();

		List<MatchingReport> reports = cPlay.makePlayReports();
		cPlay.displayPlayReports(reports);

		double[] frequencyReport = cPlay.makeFrequencyReport(reports);
		cPlay.displayFrequencyReports(frequencyReport);

		double[] hitReports = cPlay.makeHitReports(frequencyReport);
		System.out.println("hitReport " + 3 + ": " + cPlay.reportHitReports(hitReports));

		System.out.println("\n~~~ FINISH ~~~");
		System.out.println(timer.reportExtended());
	}

	public List<MatchingReport> makePlayReports() {
		List<MatchingReport> reports = new ArrayList<>();
		Combinator combinator = new Combinator(combSetSize, gameSetSize);

		while (!combinator.isFinished()) {
			int[] playCombination = combinator.makeNext();
			MatchingReport analizReport = histAnalyzer.makeMatchingReport(playCombination, "-");
			int[] matching = analizReport.getMatching();
			if (       matching[matching.length - 1] <= matchingMask[4] 
					&& matching[matching.length - 2] <= matchingMask[3]
					&& matching[matching.length - 3] <= matchingMask[2]
					&& matching[matching.length - 4] <= matchingMask[1]
					&& matching[matching.length - 5] <= matchingMask[0]
				) 
			{
				reports.add(analizReport);
			}
		}
		return reports;
	}

	public void displayPlayReports(List<MatchingReport> reports) {
		int counter = 0;
		for (MatchingReport report : reports) {
			String pn = ++counter < 10 ? "00" + counter : counter < 100 ? "0" + counter : "" + counter;
			String labe = "  " + combSetSize + "/" + gameSetSize;
			System.out.println(pn + labe + "\t" + report.report());
		}
	}

	public double[] makeFrequencyReport(List<MatchingReport> matchingReports) {
		// Count Frequency of balls in matchingReports
		int[] counter = new int[gameType.getGameSetSize() + 1];
		for (MatchingReport report : matchingReports) {
			for (int ball : report.getPlayCombination()) {
				++counter[ball];
			}
		}
		// prepare report
		double[] freqReport = new double[gameType.getGameSetSize() + 1];
		for (int i = 1; i < counter.length; i++) {
			freqReport[i] = counter[i] + 0.01 * i;
		}
		Arrays.sort(freqReport);

		return freqReport;
	}

	public void displayFrequencyReports(double[] freqReport) {
		int c = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = freqReport.length - 1; i >= 0; i--) {
			String ball = Serv.normInt2((int) (0.5 + 100.0 * (freqReport[i] - (int) freqReport[i])));
			String frequency = Serv.normInt3((int) freqReport[i]);
			sb.append(++c).append("|").append(frequency).append("(").append(ball).append(")").append("  ");
		}
		// System.out.println("Frequency of balls in matchingReports: frequency.ball");
		System.out.println(histShift + " > " + sb.toString());
	}

	public double[] makeHitReports(double[] frequencyReport) {

		int bigSize = 3;
		int smallFreq = 0;

		int[] bigFrequencySet = makeBigFrequencySet(frequencyReport, bigSize);

		int[] smallFrequencySet = makeSmallFrequencySet(frequencyReport, smallFreq);

		int[] nextLineOfHistBlock = histAnalyzer.getNextLineOfHistBlock(histShift);

		int bigHitCount = countHits(bigFrequencySet, nextLineOfHistBlock);

		int smallHitCount = countHits(smallFrequencySet, nextLineOfHistBlock);

		double[] result = new double[2];
		result[0] = bigHitCount + 0.01 * bigFrequencySet.length;
		result[1] = smallHitCount + 0.01 * smallFrequencySet.length;

		return result;
	}

	public String reportHitReports(double[] hitReports) {
		StringBuilder sb = new StringBuilder();
		for (double r : hitReports) {
			int hitCount = (int) r;
			int setSize = (int) (0.5 + 100 * (r - (int) r));
			double hitCoef = 1.0 * hitCount / setSize;
			String bigHitReport = String.format(" %d/%d-%s ", hitCount, setSize, Serv.normDouble4(hitCoef));
			sb.append(bigHitReport).append("  ");
		}
		return sb.toString();
	}

	private int[] makeBigFrequencySet(double[] frequencyReport, int n) {
		int[] bigFrequencySet = new int[n];
		int lastPozytion = frequencyReport.length - 1;
		for (int i = lastPozytion, counter = 0; counter < n && i >= 0; i--, counter++) {
			bigFrequencySet[counter] = (int) (0.5 + 100.0 * (frequencyReport[i] - (int) frequencyReport[i]));
		}
		return bigFrequencySet;
	}

	private int[] makeSmallFrequencySet(double[] frequencyReport, int n) {
		List<Integer> smallFrequencySet = new ArrayList<>();
		for (int i = 0; i < frequencyReport.length; i++) {
			if (frequencyReport[i] > n + 1.0) {
				break;
			}
			int ball = (int) (0.5 + 100.0 * (frequencyReport[i] - (int) frequencyReport[i]));
			if (ball == 0) {
				continue;
			}
			smallFrequencySet.add(ball);
		}
		int[] result = new int[smallFrequencySet.size()];
		for (int i = 0; i < smallFrequencySet.size(); i++) {
			result[i] = smallFrequencySet.get(i);
		}
		return result;
	}

	private int countHits(int[] a, int[] b) {
		int hitCount = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				if (a[i] == b[j]) {
					++hitCount;
				}
			}
		}
		return hitCount;
	}
}
