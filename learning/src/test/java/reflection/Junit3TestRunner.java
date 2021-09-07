package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = new Junit3Test();
        Arrays.stream(clazz.getMethods())
            .filter(method -> method.getName().startsWith("test"))
            .forEach(method -> invoke(junit3Test, method));
    }

    private Object invoke(Junit3Test test, Method method) {
        try {
            return method.invoke(test);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
