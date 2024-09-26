package reflection;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

	@Test
	void run() throws Exception {
		// Junit3Test 클래스를 가져옴
		Class<Junit3Test> clazz = Junit3Test.class;

		// Junit3Test의 인스턴스 생성
		Junit3Test instance = clazz.getDeclaredConstructor().newInstance();

		// 클래스의 모든 메서드를 가져와서
		Method[] methods = clazz.getDeclaredMethods();

		// 메서드 이름이 "test"로 시작하는 메서드를 필터링하여 실행
		for (Method method : methods) {
			if (method.getName().startsWith("test")) {
				method.invoke(instance);  // 메서드 실행
			}
		}
	}
}
