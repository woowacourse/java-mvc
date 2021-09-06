package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = new Junit3Test();
        Method methods[] = clazz.getDeclaredMethods();

        for (Method m: methods) {
            if (m.getName().startsWith("test")) {
                m.invoke(junit3Test);
            }
        }
    }
}
