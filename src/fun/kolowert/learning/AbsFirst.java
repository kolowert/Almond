package fun.kolowert.learning;

public abstract class AbsFirst {
	
	protected int id;
	protected String name;
	
	abstract boolean isNamed();
	
	abstract void setIdAndName(int i, String n);
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
}
