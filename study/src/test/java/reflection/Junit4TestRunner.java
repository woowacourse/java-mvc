package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() {
        Class<Junit4Test> clazz = Junit4Test.class;

        Arrays.stream(clazz.getMethods()) // Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
                .filter(this::hasAnnotation)
                .forEach(method -> invokeTestMethod(clazz, method));
    }

    private boolean hasAnnotation(Method method) {
        return method.isAnnotationPresent(MyTest.class);
    }

    private <T> void invokeTestMethod(Class<T> clazz, Method method) {
        try {
            T instance = clazz.getConstructor().newInstance();
            method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
