package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        final Constructor<Junit3Test> declaredConstructor = clazz.getDeclaredConstructor();
        final Junit3Test junit3TestInstance = declaredConstructor.newInstance();
        final Method[] declaredMethods = clazz.getDeclaredMethods();

        final List<Method> testMethods = Arrays.stream(declaredMethods)
                .filter(method -> method.getName().startsWith("test"))
                .collect(Collectors.toList());

        for (Method testMethod : testMethods) {
            testMethod.invoke(junit3TestInstance);
        }
    }
}
