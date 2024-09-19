package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test instance = clazz.getDeclaredConstructor().newInstance();

        Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> invokeMethod(method, instance));
    }

    private void invokeMethod(Method method, Junit3Test instance) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
