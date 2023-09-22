package reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(clazz.getDeclaredConstructor().newInstance());
                    } catch (final IllegalAccessException | InvocationTargetException | InstantiationException |
                                   NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
