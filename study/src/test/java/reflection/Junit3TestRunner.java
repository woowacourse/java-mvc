package reflection;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        final Junit3Test instance = clazz.getConstructor().newInstance();
        final List<Method> testMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .collect(Collectors.toList());
        for (final Method testMethod : testMethods) {
            testMethod.invoke(instance);
        }
    }
}
