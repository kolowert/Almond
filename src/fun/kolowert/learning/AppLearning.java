package fun.kolowert.learning;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.Properties;

public class AppLearning {

	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		AppLearning learning = new AppLearning();

		learning.playInheritance(false);

		learning.playClass(false);

		learning.playString(false);

		learning.playSystemProperties(true);

		learning.playWrappers(false);

		learning.playClassLoader(false);

	}

	private void playClassLoader(boolean doit) {
		if (!doit)
			return;

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		System.out.println("classLoader: " + classLoader.toString());
	}

	@SuppressWarnings("deprecation")
	private void playWrappers(boolean doit) {
		if (!doit)
			return;

		boolean bool2 = false;

		Boolean boolean1 = new Boolean("TRUE");
		Boolean boolean2 = new Boolean(bool2);

		System.out.println(boolean1 + " " + boolean2);
	}

	private void playString(boolean doit) {
		if (!doit)
			return;

		String s1 = "Hello world";
		String s2 = "Hello world";
		String s3 = new String("Hello world");
		String s4 = new String(s1);

		System.out.println("String`s equally = " + (s1.equals(s2)));
		System.out.println("s1 and s2 are the same = " + (s1 == s2));
		System.out.println("s1 and s3 are the same = " + (s1 == s3));
		System.out.println("s1 and s4 are the same = " + (s1 == s4));

	}

	private void playSystemProperties(boolean doit) {
		if (!doit)
			return;

		Properties properties = System.getProperties();
		String prop = properties.toString();
		System.out.println("\nSystem Properties");
		System.out.println(prop.replace(", ", ",\n").replace(";", ";\n\t"));
	}

	@SuppressWarnings("deprecation")
	private void playClass(boolean doit)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
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

		System.out.println("first.getClass().getCanonicalName() -> " + first.getClass().getCanonicalName());
		@SuppressWarnings("unchecked")
		Class<First> forNameFirst = (Class<First>) Class.forName("fun.kolowert.learning.First");
		System.out.println("forNameFirst.newInstance() : " + forNameFirst.newInstance().toString());

		Field[] fields = first.getClass().getFields();
		System.out.println("fields: " + fields + "  size: " + fields.length);
		for (Field f : fields) {
			System.out.println("~~~");
			System.out.println("f: " + f.toString());
		}
	}

	/**
	 * playInheritance
	 */
	private void playInheritance(boolean doit) {
		if (!doit)
			return;

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
