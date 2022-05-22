package fun.kolowert.cplayer;

import fun.kolowert.common.GameType;
import fun.kolowert.common.MatchingReporter;
import fun.kolowert.serv.Serv;

public class CoupleHitReporter {
	
	private final GameType gameType;
	private final int histShift;
	private double[] frequencyReport;
	private boolean isReportMade;
	
	public CoupleHitReporter(GameType gameType, int histShift, double[] frequencyReport) {
		this.gameType = gameType;
		this.histShift = histShift;
		this.frequencyReport = frequencyReport;
	}
	
	public double[] getReport() {
		if (!isReportMade) {
			frequencyReport = makeReport();
			isReportMade = true;
			return frequencyReport;
		}
		return frequencyReport;
	}
	
	private double[] makeReport() {
		
		// obtain next game-history line
		MatchingReporter histAnalyzer = new MatchingReporter(gameType, 1, histShift);
		int[] nextLineOfHistBlock = histAnalyzer.getHistHandler().getNextLineOfHistBlock(histShift);
		
		// obtain balls from frequency report
		int qFreqReport = frequencyReport.length;
		int qBalls = 5;
		int[] balls = new int[qBalls];
		for (int i = 0; i < qBalls; i++) {
			double freqReportUnit = 0.005 + frequencyReport[qFreqReport - 1 - i]; 
			balls[i] = (int) (100 * (freqReportUnit - (int) freqReportUnit)); 
		}
		
		//System.out.println(Arrays.toString(balls));
		
		// count hits
		double[] result = new double[(int) Combinator.calculateCombinations(2, qBalls)]; 
		int position = 0;
		for (int a = 0; a < qBalls - 1; a++) {
			for (int b = a + 1; b < qBalls; b++) {
				
				int counter = 0;
				for (int h : nextLineOfHistBlock) {
					if (balls[a] == h) {
						++counter;
					}
					if (balls[b] == h) {
						++counter;
					}
				}
				result[position++] = 100 * balls[a] + balls[b] + 0.1 * counter;
			}
		}
		
		//System.out.println(Arrays.toString(result));
		
		return result;
	}
	
	public void displayReport() {
		double[] report = getReport();
		StringBuilder sb = new StringBuilder(64);
		
		for (double r : report) {
			int aBall = (int) (0.01 * r);
			int bBall = (int) (r - 100 * aBall);
			int hits = (int) (0.05 + 10 * (r - (int) r));
			sb
				.append(hits)
				.append("(")
				.append(Serv.normInt2(aBall))
				.append("-")
				.append(Serv.normInt2(bBall))
				.append(") ");
		}
		
		System.out.println("CoupleHits " + histShift + " >> " + sb.toString());
	}
	
	// debugging
	public static void main(String[] args) {
		double[] freqReport = new double[] { 325.02, 295.23, 293.76, 282.45, 276.75, 233.31, 219.39 };
		CoupleHitReporter chr = new CoupleHitReporter(GameType.KENO, 3, freqReport);
		chr.displayReport();
	}
}
