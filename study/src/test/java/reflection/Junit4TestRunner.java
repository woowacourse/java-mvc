package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<?> clazz = Class.forName("reflection.Junit4Test");
        Object instance = clazz.getDeclaredConstructor().newInstance();

        for(Method method: clazz.getDeclaredMethods()) {
            if(method.isAnnotationPresent(MyTest.class)) {
                method.invoke(instance);
            }
        }
    }
}
