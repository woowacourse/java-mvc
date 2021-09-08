package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final List<Method> methods = Arrays.stream(clazz.getMethods())
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .collect(Collectors.toList());

        final List<Method> expected = Arrays.asList(
            clazz.getMethod("one"),
            clazz.getMethod("two")
        );

        for (Method method : methods) {
            method.invoke(new Junit4Test());
        }

        assertThat(methods).isEqualTo(expected);
    }
}
