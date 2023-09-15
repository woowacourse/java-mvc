package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final Junit4Test junit4Test = new Junit4Test();
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            final Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(MyTest.class)) {
                    method.invoke(junit4Test);
                }
            }
        }
    }
}
