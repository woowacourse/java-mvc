package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        List<Method> testMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .toList();

        assertThat(testMethods).extracting(Method::getName)
                .containsExactlyInAnyOrder("test1", "test2");

        Junit3Test instance = clazz.getDeclaredConstructor().newInstance();
        for (Method method : testMethods) {
            method.invoke(instance);
        }
    }
}
