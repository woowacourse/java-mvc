package reflection;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

class Junit3TestRunner {

    private static final Logger log = LoggerFactory.getLogger(Junit3TestRunner.class);

    @ParameterizedTest
    @ValueSource(strings = {
            "test1",
            "test2",
            "three"
    })
    void run(final String methodName) throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Method foundMethod = clazz.getMethod(methodName);

        log.info("=================> methodName = {}", methodName);
        log.info("=================> foundMethod = {}", foundMethod);

        foundMethod.invoke(new Junit3Test());
    }
}
