package fun.kolowert.learning.cloning;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Cloner implements Cloneable, Serializable {

	private static final long serialVersionUID = 1650737543L;

	private int id;
	private String date;
	protected List<String> basket;
	protected HashMap<Integer, String> props;

	public Cloner() {
		basket = new ArrayList<String>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getBasket() {
		return basket;
	}

	public void setBasket(List<String> basket) {
		this.basket = basket;
	}

	public HashMap<Integer, String> getProps() {
		return props;
	}

	public void setProps(HashMap<Integer, String> props) {
		this.props = props;
	}

	public void addGoods(final String[] list) {
		basket.addAll(Arrays.asList(list));

		HashMap<Integer, String> hm = new HashMap<>();
		for (int i = 0; i < list.length; i++)
			hm.put(Integer.valueOf(i + 1), list[i]);
		setProps(hm);
	}

	public void addGood(final String good) {
		basket.add(good);
		props.put(Integer.valueOf(props.size() + 1), good);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Cloner clonned = null;
		try {
			ByteArrayOutputStream bos;
			ObjectOutputStream ous;
			bos = new ByteArrayOutputStream();
			ous = new ObjectOutputStream(bos);
			ous.writeObject(this);
			ous.close();

			ByteArrayInputStream bis;
			ObjectInputStream ois;
			bis = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bis);
			clonned = (Cloner) ois.readObject();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clonned;
	}

}
