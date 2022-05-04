package fun.kolowert.simple;

public class Second {
	
	private int id;
	private String s;
	
	public Second(int number, String input) {
		id = number;
		s = input;
	}
	
	public void report() {
		System.out.println("Second " + id + " : " + s);
	}
	
}
