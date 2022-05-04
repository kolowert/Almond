package fun.kolowert.simple;

public class App {

	public static void main(String[] args) {
		System.out.println("hello Simple!");
		if (args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				Second next = new Second(i, args[i]);
				next.report();
			}
		} else {
			Second next = new Second(-1, "no way");
			next.report();
		}
	}

}

class Next {

	private int id;
	private String s;

	public Next(int number, String input) {
		id = number;
		s = input;
	}

	public void report() {
		System.out.println("number " + id + " : " + s);
	}
}
