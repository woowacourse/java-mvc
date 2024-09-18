package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test testInstance = clazz.getDeclaredConstructor().newInstance();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        for (Method method : clazz.getDeclaredMethods()) {
            invokeTestMethods(method, testInstance);
        }
    }

    private static void invokeTestMethods(Method method, Junit3Test testInstance)
            throws IllegalAccessException, InvocationTargetException {
        if (method.getName().startsWith("test")) {
            method.invoke(testInstance);
        }
    }
}
