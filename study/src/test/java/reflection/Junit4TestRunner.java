package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit4Test> clazz = Junit4Test.class;
        final Junit4Test junit4Test = new Junit4Test();

        for (final Method method : clazz.getMethods()) {
            if (method.getDeclaredAnnotation(MyTest.class) != null) {
                method.invoke(junit4Test);
            }
        }
    }
}
