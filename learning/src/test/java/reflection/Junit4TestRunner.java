package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Junit4TestRunner {

    private static final Logger LOG = LoggerFactory.getLogger(Junit4Test.class);

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Junit4Test instance = clazz.getDeclaredConstructor().newInstance();

        Method[] declaredMethods = clazz.getDeclaredMethods();
        Arrays.stream(declaredMethods)
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .forEach(method -> {
                try {
                    method.invoke(instance);
                } catch (Exception e) {
                    LOG.error("method error : {}", e.getMessage());
                }
            });
    }
}
