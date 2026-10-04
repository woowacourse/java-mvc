package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<?> clazz = Class.forName("reflection.Junit3Test");
        Constructor<?> ctor = clazz.getDeclaredConstructor();
        Object obj = ctor.newInstance();

        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("test") && method.getParameterCount() == 0) {
                method.invoke(obj);
            }
        }
    }
}
