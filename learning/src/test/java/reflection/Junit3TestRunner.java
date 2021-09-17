package reflection;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        Junit3Test instance = new Junit3Test();

        Class<Junit3Test> clazz = Junit3Test.class;

        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.getName().contains("test")) {
                return;
            }

            method.invoke(instance);
        }
    }
}
