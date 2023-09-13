package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        final Junit4Test junit4Test = new Junit4Test();

        final List<Method> myTestMethods = Arrays.stream(clazz.getMethods())
            .filter(method -> method.getAnnotation(MyTest.class) != null)
            .collect(Collectors.toList());

        for (final Method myTestMethod : myTestMethods) {
            myTestMethod.invoke(junit4Test);
        }
    }
}
