package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void runOneMethod() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Method method = clazz.getMethod("test1");

        method.invoke(clazz.getConstructor().newInstance());
        // TODO 하나의 메소드를 타겟하여 실행
    }

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(clazz.getConstructor().newInstance());
            }
        }
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
    }
}
