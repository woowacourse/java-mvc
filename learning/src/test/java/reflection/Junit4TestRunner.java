package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit4Test> clazz = Junit4Test.class;
        final Constructor<Junit4Test> declaredConstructor = clazz.getDeclaredConstructor();
        final Junit4Test junit4TestInstance = declaredConstructor.newInstance();
        final Reflections reflections = new Reflections(clazz, new MethodAnnotationsScanner());

        final Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(MyTest.class);

        for (Method method : methodsAnnotatedWith) {
            method.invoke(junit4TestInstance);
        }
    }
}
