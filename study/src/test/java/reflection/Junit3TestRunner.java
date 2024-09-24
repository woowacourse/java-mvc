package reflection;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = mock(Junit3Test.class);

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

        assertAll(
                () -> verify(junit3Test).test1(),
                () -> verify(junit3Test).test2(),
                () -> verify(junit3Test, never()).three()
        );
    }
}
