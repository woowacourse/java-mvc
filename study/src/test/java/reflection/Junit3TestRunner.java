package reflection;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = clazz.getConstructor().newInstance();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(junit3Test, (Object[]) null);
            }
        }
    }
}
