package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<?> clazz = Junit3Test.class;

        var testInstance = clazz.getDeclaredConstructor().newInstance();
        Method[] methods = testInstance.getClass().getMethods();

        for (var x : methods) {
            if (x.getName().startsWith("test")) {
                x.invoke(testInstance);
            }
        }
    }
}
