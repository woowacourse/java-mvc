package reflection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class Junit3TestRunner {

    private final OutputCapture outputCapture = new OutputCapture();

    @BeforeEach
    void setUpStreams() {
        outputCapture.startCapture();
    }

    @AfterEach
    void restoreStreams() {
        outputCapture.stopCapture();
    }

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junitTest = new Junit3Test();

        // Junit3Test에서 test로 시작하는 메소드 실행
        List<Method> testMethods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .toList();

        for (Method method : testMethods) {
            method.invoke(junitTest);
        }

        outputCapture.assertContains("Running Test1\n", "Running Test2\n");
        outputCapture.assertDoseNotContain("Running Test3\n");
    }
}
