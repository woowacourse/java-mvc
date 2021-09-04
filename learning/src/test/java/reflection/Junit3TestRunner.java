package reflection;

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
        List<Method> runMethods = Arrays.stream(clazz.getMethods())
            .filter(method -> method.isAnnotationPresent(Test.class))
            .collect(Collectors.toList());

        Junit3Test junit3Test = new Junit3Test();

        for (Method method : runMethods) {
            method.invoke(junit3Test);
        }
    }
}
