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

        Junit4Test instance = clazz.newInstance();
        Method[] methods = clazz.getMethods();

        List<Method> methodsContainedAnnotation = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .collect(Collectors.toList());

        for (Method method : methodsContainedAnnotation) {
            try {
                method.invoke(instance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
