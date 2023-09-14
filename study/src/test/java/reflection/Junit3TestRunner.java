package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3TestInstance = clazz.getDeclaredConstructor().newInstance();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Method[] methods = clazz.getMethods();
        Arrays.stream(methods).forEach(m -> startWithName(m, junit3TestInstance));
    }

    private void startWithName(Method method, Object obj) {
        if (method.getName().startsWith("test")) {
            try {
                method.invoke(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

