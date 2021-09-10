package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        final List<Method> methods = Arrays.stream(clazz.getMethods())
            .filter(method -> method.getName().startsWith("test"))
            .collect(Collectors.toList());

        final List<Method> expected = Arrays.asList(
            clazz.getMethod("test1"),
            clazz.getMethod("test2")
        );

        for (Method method : methods) {
            method.invoke(new Junit3Test());
        }

        assertThat(methods).isEqualTo(expected);
    }
}
