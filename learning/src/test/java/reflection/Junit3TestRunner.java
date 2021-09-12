package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Junit3TestRunner {

    private static final Logger LOG = LoggerFactory.getLogger(Junit3Test.class);

    @Test
    void run() throws Exception {

        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test 에서 test 로 시작하는 메소드 실행

        Junit3Test instance = clazz.getDeclaredConstructor().newInstance();
        Method[] declaredMethods = clazz.getDeclaredMethods();

        Arrays.stream(declaredMethods)
            .filter(method -> method.getName().startsWith("test"))
            .forEach(method -> {
                try {
                    method.invoke(instance);
                } catch (Exception e) {
                    LOG.error("method error : {}", e.getMessage());
                }
            });
    }
}
