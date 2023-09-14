package reflection;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getConstructor().newInstance();
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            for (Annotation annotation : declaredMethod.getAnnotations()) {
                if (annotation instanceof MyTest) {
                    declaredMethod.invoke(junit4Test);
                }
            }
        }
    }
}
