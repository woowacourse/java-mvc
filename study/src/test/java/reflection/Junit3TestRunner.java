package reflection;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        /**
         * 구체 클래스를 명시하지 않아도 실행할 수 있다.
         */
        Class<?> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        final Object instance = clazz.getDeclaredConstructor().newInstance();
        Method[] methods = clazz.getDeclaredMethods();

        // "test"로 시작하는 메소드 실행
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(instance);  // 메소드 실행
            }
        }
    }
}
