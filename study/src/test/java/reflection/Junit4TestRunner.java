package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.AnnotationUtils;

class Junit4TestRunner {

    @DisplayName("@MyTest 어노테이션이 붙은 메서드를 모두 실행한다")
    @Test
    void runAnnotatedTestMethods() {
        Class<Junit4Test> clazz = Junit4Test.class;

        Arrays.stream(clazz.getMethods()) // Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
                .filter(this::hasAnnotation)
                .forEach(method -> invokeTestMethod(clazz, method));
    }

    @DisplayName("@MyTest 어노테이션이 붙은 메서드를 AnnotationUtils로 찾아 모두 실행한다")
    @Test
    void runAnnotatedTestMethodsUsingAnnotationUtils() {
        Class<Junit4Test> clazz = Junit4Test.class;

        Arrays.stream(clazz.getMethods())
                .filter(this::hasAnnotationUsingAnnotationUtils)
                .forEach(method -> invokeTestMethod(clazz, method));
    }

    private boolean hasAnnotation(Method method) {
        return method.isAnnotationPresent(MyTest.class);
    }

    private boolean hasAnnotationUsingAnnotationUtils(Method method) {
        return AnnotationUtils.findAnnotation(method, MyTest.class).isPresent();
    } // 메타 어노테이션이나 상속 받은 어노테이션도 탐색 가능

    private <T> void invokeTestMethod(Class<T> clazz, Method method) {
        try {
            T instance = clazz.getConstructor().newInstance();
            method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
