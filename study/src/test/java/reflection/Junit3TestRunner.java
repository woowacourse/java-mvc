package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드
        for (Method m : clazz.getMethods()) {
            if (m.getName().startsWith("test")){
                m.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }
    }
}
