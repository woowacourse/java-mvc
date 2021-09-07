package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Arrays.stream(clazz.getMethods()).filter(method -> hasAnnotation(method, MyTest.class))
        .forEach(method -> {
            try {
                method.invoke(new Junit4Test());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean hasAnnotation(final Method method, final Class<?> annotationType) {
        return Arrays.stream(method.getAnnotations())
            .anyMatch(annotation -> annotation.annotationType().equals(annotationType));
    }
}
