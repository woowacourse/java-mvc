package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test target = new Junit3Test();

        Method[] declaredMethod = clazz.getDeclaredMethods();

        for (Method method : declaredMethod) {
            boolean test = method.getName().startsWith("test");
            if (test) {
                method.invoke(target);
            }
        }

    }
}
