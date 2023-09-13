package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = new Junit3Test();
        Method[] getMethodsByStartWithTest = clazz.getDeclaredMethods();
        for (Method method : getMethodsByStartWithTest) {
            if (method.getName().startsWith("test")) {
                method.invoke(junit3Test, null);
            }
        }
    }
}
