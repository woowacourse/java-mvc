package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        List<Method> testMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .toList();

        assertThat(testMethods).extracting(Method::getName)
                .containsExactlyInAnyOrder("one", "two");

        Junit4Test instance = clazz.getDeclaredConstructor().newInstance();
        for (Method method : testMethods) {
            method.invoke(instance);
        }
    }
}
