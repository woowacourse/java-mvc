package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final var methods = clazz.getDeclaredMethods();
        final var constructor = clazz.getDeclaredConstructor();
        for (Method method : methods) {
            invokeMethodsAnnotatedMyTest(constructor, method);
        }
    }

    private void invokeMethodsAnnotatedMyTest(Constructor<Junit4Test> constructor, Method method)
            throws IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        if (method.isAnnotationPresent(MyTest.class)) {
            method.invoke(constructor.newInstance());
        }
    }
    
}
