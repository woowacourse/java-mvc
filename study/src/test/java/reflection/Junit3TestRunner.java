package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = new Junit3Test();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        List<Method> methods = List.of(clazz.getDeclaredMethods());
        methods.stream()
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(junit3Test);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
