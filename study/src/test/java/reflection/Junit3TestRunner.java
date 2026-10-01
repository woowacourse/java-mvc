package reflection;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Object instance = clazz.getDeclaredConstructor().newInstance();

        java.lang.reflect.Method method1 = clazz.getMethod("test1");
        java.lang.reflect.Method method2 = clazz.getMethod("test2");
        java.lang.reflect.Method method3 = clazz.getMethod("three");

        method1.invoke(instance);
        method2.invoke(instance);
        method3.invoke(instance);
    }
}
