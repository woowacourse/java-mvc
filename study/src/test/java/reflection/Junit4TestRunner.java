package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            final MyTest annotation = method.getAnnotation(MyTest.class);
            if (annotation != null) {
                final Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();
                method.invoke(junit4Test);
            }
        }
    }
}
