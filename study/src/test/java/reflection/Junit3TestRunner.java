package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Method[] methods = clazz.getDeclaredMethods();
        Junit3Test instance = clazz.getDeclaredConstructor().newInstance();
        String prefix = "test";
        for (Method method : methods) {
            if (method.getName().startsWith(prefix)) {
                method.invoke(instance);
            }
        }
    }
}
