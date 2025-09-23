package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        final Object instance = clazz.getDeclaredConstructor().newInstance();
        final Method[] junit3TestMethods = clazz.getDeclaredMethods();
        for (Method method : junit3TestMethods) {
            if (method.getName().startsWith("test")) {
                method.invoke(instance);
            }
        }
    }
}
