package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        final List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().startsWith("test"))
                .toList();

        for (Method method : methods) {
            method.invoke(new Junit3Test());
        }
    }
}
