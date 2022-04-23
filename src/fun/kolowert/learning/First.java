package fun.kolowert.learning;

public class First extends AbsFirst implements Selectable {

	private int id;
	private String name;
	private boolean isSelected;

	public First(int id, String name, boolean isSelected) {
		this.id = id;
		this.name = name;
		this.isSelected = isSelected;
	}

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	boolean isNamed() {
		if (name != null) {
			return true;
		}
		return false;
	}
	
	@Override
	void setIdAndName(int i, String n) {
		id = i;
		name = n;
	}
	
	@Override
	public String toString() {
		return "First [id=" + id + ", name=" + name + ", isSelected=" + isSelected + "]";
	}



}
