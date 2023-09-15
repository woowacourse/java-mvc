package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        PrintTestUtil.assertThatOutputOf(() -> {
            List<Method> methods = Arrays.stream(clazz.getMethods())
                    .filter(method -> method.isAnnotationPresent(MyTest.class))
                    .collect(Collectors.toList());

            for (Method method : methods) {
                method.invoke(new Junit4Test());
            }
        }).isEqualTo("Running Test1\n" + "Running Test2\n");
    }
}
