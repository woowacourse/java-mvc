package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        final var methods = clazz.getDeclaredMethods();
        final var constructor = clazz.getDeclaredConstructor();
        for (Method method : methods) {
            invokeMethodsByName(constructor, method);
        }
    }

    private void invokeMethodsByName(Constructor<Junit3Test> constructor, Method method)
            throws IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        final var name = method.getName();
        if (name.startsWith("test")) {
            method.invoke(constructor.newInstance());
        }
    }
    
}
