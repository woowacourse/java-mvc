package reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Junit4TestRunner {

    private static final Logger log = LoggerFactory.getLogger(Junit4TestRunner.class);

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final List<Method> foundMethodsWithMyTestAnnotation = Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .collect(Collectors.toList());

        for (final Method foundMethod : foundMethodsWithMyTestAnnotation) {
            log.info("=================> methodName = {}", foundMethod.getName());
            log.info("=================> foundMethod = {}", foundMethod);
            foundMethod.invoke(new Junit4Test());
        }
    }
}
