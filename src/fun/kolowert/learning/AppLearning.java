package fun.kolowert.learning;

import java.lang.reflect.AnnotatedType;

public class AppLearning {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		AppLearning learning = new AppLearning();

		learning.playInheritance(false);

		learning.playClass(true);

	}

	private void playClass(boolean doit) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (!doit)
			return;
		
		Selectable selectable = new First(1, "Name of Selectable", true);
		
		AnnotatedType annotatedSuperclassSelectable = selectable.getClass().getAnnotatedSuperclass();
		System.out.println("annotatedSuperclassSelectable " + annotatedSuperclassSelectable);
		
		First first = new First(1, "Name of First", true);
		AnnotatedType annotatedSuperclassFirst = first.getClass().getAnnotatedSuperclass();
		System.out.println("annotatedSuperclassFirst " + annotatedSuperclassFirst);
		
		AnnotatedType[] annotatedInterfaces = first.getClass().getAnnotatedInterfaces();
		for (AnnotatedType at : annotatedInterfaces) {
			System.out.println("at: " + at.getClass().getTypeName());
		}
		
		System.out.println(first.getClass().getCanonicalName());
		Class<First> forNameFirst = (Class<First>) Class.forName("fun.kolowert.learning.First");
		System.out.println(forNameFirst.newInstance().toString());
	}
	
	/**
	 * playInheritance
	 */
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
