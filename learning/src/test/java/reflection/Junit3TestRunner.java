package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        // given
        Class<Junit3Test> clazz = Junit3Test.class;
        Method[] methods = clazz.getDeclaredMethods();
        Constructor<Junit3Test> constructor = clazz.getConstructor();
        Junit3Test junit3Test = constructor.newInstance();

        // when, then
        Arrays.stream(methods)
            .forEach(execute(junit3Test));
    }

    private Consumer<Method> execute(Junit3Test junit3Test) {
        return method -> {
            method.setAccessible(true);
            try {
                method.invoke(junit3Test);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        };
    }
}
