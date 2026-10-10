package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Method[] methods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .toArray(Method[]::new);

        for (Method method : methods) {
            method.invoke(clazz.getDeclaredConstructor().newInstance());
        }

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
    }
}
