package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> runTest(method, junit3Test));
    }

    private void runTest(final Method method, final Object instance) {
        try {
            method.invoke(instance);
        } catch (Exception e) {
            throw new IllegalStateException(method.getName() + " 실행에 실패했습니다.", e);
        }
    }
}
