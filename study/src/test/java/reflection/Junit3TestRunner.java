package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // Junit3Test에서 test로 시작하는 메소드 실행
        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("test")) {
                Junit3Test targetClass = clazz.getDeclaredConstructor().newInstance();
                method.invoke(targetClass);
            }
        }
    }
}
