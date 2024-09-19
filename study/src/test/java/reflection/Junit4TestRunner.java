package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Junit4Test instance = clazz.getDeclaredConstructor().newInstance();

        Arrays.stream(clazz.getMethods())
                .filter(method -> Arrays.asList(method.getAnnotations()).stream()
                        .anyMatch(annotation -> annotation.annotationType().equals(MyTest.class)))
                .forEach(method -> invokeMethod(method, instance));
    }

    private void invokeMethod(Method method, Junit4Test instance) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
