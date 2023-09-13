package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Arrays.stream(clazz.getMethods())
                .filter(method ->
                        method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(new Junit3Test());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
