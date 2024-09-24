package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        List<Method> methods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .toList();
        List<String> methodNames = methods.stream()
                .map(Method::getName)
                .toList();

        for (Method method : methods) {
            method.invoke(clazz.getDeclaredConstructor().newInstance());
        }

        assertThat(methodNames).containsExactly("test1", "test2");
    }
}
