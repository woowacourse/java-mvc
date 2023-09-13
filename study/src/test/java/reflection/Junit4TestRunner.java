package reflection;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit4Test> clazz = Junit4Test.class;
        final Constructor<Junit4Test> constructor = clazz.getConstructor();
        final Junit4Test junit4Test = constructor.newInstance();

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(junit4Test);
            }
        }
    }
}
