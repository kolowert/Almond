package fun.kolowert.bplayer;

import java.util.ArrayList;
import java.util.List;

import fun.kolowert.common.GameType;
import fun.kolowert.common.HistHandler;
import fun.kolowert.serv.Serv;

public class HitAnalizer {

	/**
	 * 
	 * @param gameType
	 * @param results
	 * @param letMarkLines
	 * @param letBallLines
	 * @param hitPozysions - it is transient parameter
	 * @return Table Of Hits On Frequency
	 */
	public static List<String> makeTableOfHitsOnFrequency(GameType gameType, List<ResultSet> results,
			List<int[]> hitPozysions, boolean letMarkLines, boolean letBallLines) {

		int baseHistShift = findBaseHistShift(results);

		// prepare history & ball lines
		int histDeep = results.size();
		HistHandler histHandler = new HistHandler(gameType, histDeep + 1, baseHistShift);
		List<int[]> histCombinations = histHandler.getHistCombinations();
		List<int[]> ballLines = prepareBallLines(results, histCombinations);

		// debugging
//		for (int[] hc : histCombinations) {
//			System.out.println(java.util.Arrays.toString(hc));
//		}
//		for (int[] balls : ballLines) {
//			System.out.println(java.util.Arrays.toString(balls));
//		}
//		System.out.println();

		// find marks positions and make mark & ball lines
		List<String> markTable = new ArrayList<>();
		for (int[] balls : ballLines) {
			
			int hitIndexCounter = 0;
			int hitPozsCounter = 0;
			int[] hitPozs = new int[gameType.getPlaySetSize() + 1];
			hitPozs[0] = balls[0];

			StringBuilder markLine = new StringBuilder(Serv.normIntX(balls[0], 3, "-") + "  ");
			StringBuilder ballLine = new StringBuilder(Serv.normIntX(balls[0], 3, "0") + "  ");

			int histLineIndex = balls[1];
			int[] histCombination = findPropperHistLine(histCombinations, histLineIndex);

			for (int ballIndex = balls.length - 1; ballIndex >= 2; ballIndex--) {
				hitPozsCounter++;
				int ball = balls[ballIndex];
				if (isBallInHistCombination(ball, histCombination)) {
					hitPozs[++hitIndexCounter] = hitPozsCounter;
					markLine.append("## ");
				} else {
					markLine.append("   ");
				}
				ballLine.append(Serv.normIntX(ball, 2, "0")).append(" ");
			}

			hitPozysions.add(hitPozs);

			if (letMarkLines) {
				markTable.add(markLine.toString());
			}

			if (letBallLines) {
				markTable.add(ballLine.toString());
			}
		}

		return markTable;
	}

	private static List<int[]> prepareBallLines(List<ResultSet> results, List<int[]> histCombinations) {
		List<int[]> ballLines = new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			int indexHistShift = results.get(i).getIndexHistShift();
			double[] rawReport = results.get(i).getFrequencyReport();
			int[] balls = makeBallsLineOnFrequencyReport(indexHistShift, rawReport);
			// put histLineIndex to balls[]
			balls[1] = histCombinations.get(i + 1)[0];
			ballLines.add(balls);
		}
		return ballLines;
	}

	/**
	 * Here ballValues[0] = indexHistShift and from index 1 go balls
	 */
	private static int[] makeBallsLineOnFrequencyReport(int indexHistShift, double[] rawReport) {
		int[] ballValues = new int[rawReport.length + 1];
		ballValues[0] = indexHistShift;
		for (int i = 1; i < rawReport.length + 1; i++) {
			int ballValue = (int) (0.5 + 100.0 * (rawReport[i - 1] - (int) rawReport[i - 1]));
			ballValues[i] = ballValue;
		}
		return ballValues;
	}

	private static boolean isBallInHistCombination(int ball, int[] histCombination) {
		for (int histBall : histCombination) {
			if (ball == histBall) {
				return true;
			}
		}
		return false;
	}

	private static int[] findPropperHistLine(List<int[]> histCombinations, int histLineIndex) {
		int[] result = new int[histCombinations.get(0).length];
		for (int[] hc : histCombinations) {
			if (hc[0] == histLineIndex + 1) {
				return hc;
			}
		}
		return result;
	}

	private static int findBaseHistShift(List<ResultSet> results) {
		int min = Integer.MAX_VALUE;
		for (ResultSet rs : results) {
			if (min > rs.getIndexHistShift()) {
				min = rs.getIndexHistShift();
			}
		}
		return min > 0 ? min - 1 : min;
	}

}
