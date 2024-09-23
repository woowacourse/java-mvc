package reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class Junit4TestRunner {

    private static final Logger log = LoggerFactory.getLogger(Junit4TestRunner.class);

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test instance = new Junit4Test();

        Method[] methods = clazz.getDeclaredMethods();
        List<String> executedMethods = new ArrayList<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                log.info("메서드 실행: " + method.getName());
                method.invoke(instance);
                executedMethods.add(method.getName());
            }
        }

        assertAll(
                () -> assertThat(executedMethods).hasSize(2),
                () -> assertThat(executedMethods).doesNotContain("testThree")
        );
    }
}
