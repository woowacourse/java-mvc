package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            method.invoke(instance);
        }

        /*
        Method method1 = clazz.getDeclaredMethod("test1");
        method1.invoke(instance);

        Method method2 = clazz.getDeclaredMethod("test2");
        method2.invoke(instance);

        Method method3 = clazz.getDeclaredMethod("three");
        method3.invoke(instance);
        */
    }
}
