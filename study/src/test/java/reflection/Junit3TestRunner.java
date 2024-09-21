package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    private static final String TEST_PREFIX = "test";

    @Test
    void run() throws Exception {
        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        Object instance = new Junit3Test();
        Class<Junit3Test> clazz = Junit3Test.class;
        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith(TEST_PREFIX)) {
                method.invoke(instance);
            }
        }
    }
}
