package reflection;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        List<Method> targetMethods = Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .collect(toList());

        Junit4Test instance = new Junit4Test();
        for (Method method : targetMethods) {
            method.invoke(instance);
        }
    }
}
