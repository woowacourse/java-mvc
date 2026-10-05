package reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    method.setAccessible(true);
                    try {
                        method.invoke(clazz.getDeclaredConstructor().newInstance());
                    } catch (IllegalAccessException
                             | InvocationTargetException
                             | InstantiationException
                             | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
