package fun.kolowert.learning.cloning;

public class ClonerPlayer {

	public static void main(String[] args) {

		Cloner ct1 = new Cloner();
		Cloner ct2 = null;
		ct1.setId(1);
		ct1.setDate("01.12.2015");
		String[] goods = { "Potato", "Beet" };
		ct1.addGoods(goods);

		try {
			ct2 = (Cloner) ct1.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		ct2.addGood("Carrot");

		System.out.println("ct1 = " + ct1.toString());
		System.out.println("ct2 = " + ct2.toString());

		System.out.println();
		ct2.setId(2);
		System.out.println("ct2.id = " + ct2.getId());
		System.out.println("ct1.id == ct2.id : " + (ct1.getId() == ct2.getId()));

		System.out.println();
		System.out.println("ct2.date = " + ct2.getDate());
		System.out.println("ct1.date == ct2.date : " + (ct1.getDate().equals(ct2.getDate())));

		System.out.println();
		System.out.println("ct1.basket : " + (ct1.basket));
		System.out.println("ct2.basket : " + (ct2.basket));

		System.out.println();
		System.out.println("ct1.props : " + (ct1.props));
		System.out.println("ct2.props : " + (ct2.props));

	}

}
