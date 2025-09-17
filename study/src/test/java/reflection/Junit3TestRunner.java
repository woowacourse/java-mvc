package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Object instance = clazz.getDeclaredConstructor().newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test") &&
                    method.getParameterCount() == 0 &&
                    method.getReturnType() == void.class) {
                method.invoke(instance);
            }
        }
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
    }
}
