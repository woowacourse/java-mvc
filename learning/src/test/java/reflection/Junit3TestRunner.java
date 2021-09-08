package reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void runStartsWithTest() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = clazz.getConstructor().newInstance();

        Arrays.stream(clazz.getDeclaredMethods())
            .filter(it -> it.getName().startsWith("test"))
            .forEach(it -> {
                try {
                    it.invoke(junit3Test);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
    }

    @Test
    void runWithOutNameTest() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = clazz.getConstructor().newInstance();

        Arrays.stream(clazz.getDeclaredMethods())
            .filter(it -> !it.getName().startsWith("test"))
            .forEach(it -> {
                try {
                    it.invoke(junit3Test);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
    }
}
