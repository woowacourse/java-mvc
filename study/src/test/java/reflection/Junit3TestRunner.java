package reflection;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test instance = new Junit3Test();

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            String name = method.getName();

            if (name.startsWith("test")) {
                method.invoke(instance);
            }
        }
    }
}
