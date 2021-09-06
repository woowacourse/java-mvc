package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getConstructor().newInstance();

        Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods)
            .filter(it -> Objects.nonNull(it.getAnnotation(MyTest.class)))
            .forEach(it -> {
                try {
                    it.invoke(junit4Test);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
    }
}
