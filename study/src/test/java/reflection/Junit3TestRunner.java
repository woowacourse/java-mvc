package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        Junit3Test instance = clazz.newInstance();

        Class<?> junit3Test = clazz.getClassLoader()
                .loadClass("reflection.Junit3Test");

        Method[] methods = junit3Test.getMethods();

        List<Method> testMethod = Arrays.stream(methods)
                .filter(method -> method.getName().startsWith("test"))
                .collect(Collectors.toList());

        testMethod.forEach(method -> {
            try {
                method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
