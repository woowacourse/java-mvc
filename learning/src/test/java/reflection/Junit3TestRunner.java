package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test junit3Test = clazz.getConstructor().newInstance();

        Method[] methods = clazz.getMethods();
        Arrays.stream(methods).filter(method -> method.getName().startsWith("test")).forEach(method -> {
            try {
                method.invoke(junit3Test);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}
