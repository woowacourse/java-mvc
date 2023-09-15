package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        PrintTestUtil.assertThatOutputOf(() -> {
            List<Method> methods = Arrays.stream(clazz.getMethods()).
                    filter(method -> method.getName().startsWith("test"))
                    .collect(Collectors.toList());

            for (Method method : methods) {
                method.invoke(new Junit3Test());
            }
        }).isEqualTo("Running Test1\n" + "Running Test2\n");

    }
}
