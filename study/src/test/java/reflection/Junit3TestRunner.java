package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Object instance = clazz.getDeclaredConstructor().newInstance(); // 인스턴스 생성

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.getName().startsWith("test")) {
                method.invoke(instance);
            }
        }
    }
}
