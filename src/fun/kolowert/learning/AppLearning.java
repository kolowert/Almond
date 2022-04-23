package fun.kolowert.learning;

public class AppLearning {

	public static void main(String[] args) {

		AppLearning learning = new AppLearning();

		learning.playInheritance(true);

	}

	private void playInheritance(boolean doit) {
		if (!doit) {
			return;
		}
		
		System.out.println("~ playInheritance#AppLearning");

		Selectable first = new First(1, "Name of First", true);
		Class<?> clazz = first.getClass();
		System.out.printf("clazz %s %n", clazz);
		System.out.println(first);

		AbsFirst absFirst = new AbsFirst() {
			@Override
			boolean isNamed() {
				return false;
			}

			@Override
			void setIdAndName(int i, String n) {
				id = i;
				name = n;
			}

			@Override
			public String toString() {
				return this.getClass().getCanonicalName() + " [id=" + id + ", name=" + name + "]";
			}
		};

		Class<?> clazz2 = absFirst.getClass();
		System.out.printf("clazz2 %s %n", clazz2);
		System.out.println(absFirst);
		
		System.out.println("absFirst.hashCode() : " + absFirst.hashCode());
		absFirst = null;
		System.out.println(System.identityHashCode(absFirst));
	
	}

}
