package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] methods = clazz.getMethods();

        for (Method m : methods) {
            if (m.isAnnotationPresent(MyTest.class)) {
                m.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }
    }
}
