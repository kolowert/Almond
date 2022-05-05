package fun.kolowert.serv;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer {
	
	public static final long VERSION = 1651753200;
	private final  long started;

	public Timer() {
		this.started = System.currentTimeMillis();
	}

	public Timer(boolean displayStartTimeToConsole) {
		started = System.currentTimeMillis();
		DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
		Date startedDateTime = new Date(started);
		if (displayStartTimeToConsole) {
			System.out.println("Calculation started at: " + dateFormat.format(startedDateTime));
		}
	}

	public String reportDuration() {
		return "executed in: " + (0.001 * (System.currentTimeMillis() - started)) + " sec";
	}

	public void reportToConsole() {
		System.out.println("executed in: " + (0.001 * (System.currentTimeMillis() - started)) + " sec");
	}

	public String reportExtended() {
		return reportDuration(started);
	}

	public static String reportDuration(long startTime) {

		double rawSeconds = 0.001 * (System.currentTimeMillis() - startTime);

		StringBuilder sb = new StringBuilder();

		DecimalFormat df = new DecimalFormat("##.#####");

		if (rawSeconds <= 60.0) {
			sb.append(df.format(rawSeconds)).append(" seconds");
			return sb.toString();
		}

		double rawMinutes = rawSeconds / 60.0;
		String mins = p2(rawMinutes);
		String secs = p2(rawSeconds % 60);
		if (rawMinutes <= 60.0) {
			sb.append(mins).append(":").append(secs);
			return sb.toString();
		}

		double rawHours = rawMinutes / 60.0;
		String hours = p2(rawHours);
		if (rawHours <= 24.0) {
			sb.append(hours).append(":").append(p2(rawMinutes % 60)).append(":").append(secs);
			return sb.toString();
		}

		sb.append("longer than day !!! ").append(df.format(rawSeconds)).append(" seconds");
		return sb.toString();
	}

	private static String p2(double d) {
		int i = (int) d;
		if (i < 10) {
			return ("0" + Integer.toString(i));
		}
		return Integer.toString(i);
	}
}
