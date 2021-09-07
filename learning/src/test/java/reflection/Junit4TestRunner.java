package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = new Junit4Test();
        Arrays.stream(clazz.getMethods())
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .forEach(method -> invoke(junit4Test, method));
    }

    private Object invoke(Junit4Test test, Method method) {
        try {
            return method.invoke(test);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
