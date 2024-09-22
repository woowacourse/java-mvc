package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<?> clazz = Junit4Test.class;
        var testInstance = clazz.getDeclaredConstructor().newInstance();
        Method[] methods = testInstance.getClass().getMethods();

        // Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행

        for (Method method : methods) {
            boolean hasMyTestAnnotation = Arrays.stream(method.getAnnotations())
                    .anyMatch(annotation -> annotation.annotationType().equals(MyTest.class));
            if (!hasMyTestAnnotation) {
                continue;
            }
            try {
                method.invoke(testInstance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
