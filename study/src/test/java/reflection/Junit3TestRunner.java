package reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test junit3Test = new Junit3Test();

        Arrays.stream(clazz.getDeclaredMethods()).forEach(
                method -> {
                    try {
                        method.invoke(junit3Test);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
