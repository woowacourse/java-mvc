package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

	@Test
	void run() throws Exception {
		Class<Junit3Test> clazz = Junit3Test.class;
		Method[] methods = clazz.getMethods();
		Object instance = clazz.getDeclaredConstructor().newInstance();
		Arrays.stream(methods)
			.filter(method -> method.getName().startsWith("test"))
			.forEach(method -> {
				try {
					method.invoke(instance);
				} catch (IllegalAccessException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			});
	}
}
