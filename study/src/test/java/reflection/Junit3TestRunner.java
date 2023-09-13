package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        final Junit3Test junit3Test = clazz.getConstructor().newInstance();

        for (final Method declaredMethod : clazz.getDeclaredMethods()) {
            declaredMethod.invoke(junit3Test);
        }
    }
}
