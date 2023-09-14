package reflection;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getConstructor().newInstance();
        Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (final Annotation annotation : annotations) {
                if (annotation.annotationType().equals(MyTest.class)) {
                    method.invoke(junit4Test);
                }
            }
        }
    }
}
