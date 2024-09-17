package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                // 1. 기본 생성자를 가져와서 (getDeclaredConstructor())
                // 2. 인스턴스를 생성하고 (newInstance())
                // 3. 메서드를 실행한다. (invoke())
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }
    }
}
