package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;

        final Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            invokeMethodIfNameStartsWith("test", clazz, method);
        }
    }

    private void invokeMethodIfNameStartsWith(String methodNamePrefix, Class<?> clazz, Method method) throws Exception {
        final Object instance = clazz.getDeclaredConstructor().newInstance();
        if (method.getName().startsWith(methodNamePrefix)) {
            method.invoke(instance);
        }
    }
}
