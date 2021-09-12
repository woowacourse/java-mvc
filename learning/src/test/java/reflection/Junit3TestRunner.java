package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Constructor<Junit3Test> clazzConstructor = clazz.getConstructor();
        Junit3Test junit3Test = clazzConstructor.newInstance();

        // Junit3Test에서 test로 시작하는 메소드 실행
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (startsWithTest(method)) {
                method.invoke(junit3Test);
            }
        }
    }

    private boolean startsWithTest(Method method) {
        return getName(method).startsWith("test");
    }

    private String getName(Method method) {
        return method.getName();
    }
}
