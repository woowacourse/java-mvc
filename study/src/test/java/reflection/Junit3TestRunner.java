package reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class Junit3TestRunner {

    private static final Logger log = LoggerFactory.getLogger(Junit3TestRunner.class);

    @Test
    @DisplayName("테스트 이름이 test로 시작하는 테스트들을 실행한다.")
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test instance = new Junit3Test();

        Method[] methods = clazz.getDeclaredMethods();
        List<String> executedMethods = new ArrayList<>();

        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                log.info("메서드 실행: " + method.getName());
                method.invoke(instance);
                executedMethods.add(method.getName());
            }
        }

        assertAll(
                () -> assertThat(executedMethods).hasSize(2),
                () -> assertThat(executedMethods).doesNotContain("three")
        );
    }
}
