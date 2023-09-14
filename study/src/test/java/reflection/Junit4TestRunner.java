package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        List<Method> methods = Arrays.stream(clazz.getMethods())
            .filter(method -> haveTestAnnotation(method))
            .collect(Collectors.toList());

        Junit4Test junit4Test = clazz.getConstructor()
            .newInstance();

        for (Method method : methods) {
            method.invoke(junit4Test);
        }
    }

    private boolean haveTestAnnotation(Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
            .anyMatch(annotation -> annotation instanceof MyTest);
    }
}
