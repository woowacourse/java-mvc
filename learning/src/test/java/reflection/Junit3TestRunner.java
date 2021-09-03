package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Junit3Test junit3Test = new Junit3Test();
        Class<Junit3Test> clazz = Junit3Test.class;

        Arrays.
            stream(clazz.getMethods())
            .filter(method -> method.getName().startsWith("test"))
            .forEach(executeMethod(junit3Test));
    }

    private Consumer<Method> executeMethod(Junit3Test junit3Test) {
        return method -> {
            try {
                method.invoke(junit3Test);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        };
    }
}
