package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final Junit4Test junit4Test = clazz.getConstructor().newInstance();

        for (final Method method : clazz.getMethods()) {
            for (final Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof MyTest) {
                    method.invoke(junit4Test);
                    break;
                }
            }
        }
    }
}
