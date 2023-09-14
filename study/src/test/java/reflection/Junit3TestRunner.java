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

        final List<Method> testMethods = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().startsWith("test"))
                .collect(Collectors.toUnmodifiableList());

        Junit3Test junit3Test = clazz.getConstructor().newInstance();
        for (Method method : testMethods) {
            method.invoke(junit3Test);
        }
    }
}
