package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

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
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junitTest = new Junit4Test();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        List<Method> annotatedMethods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .toList();

        for (Method method : annotatedMethods) {
            method.invoke(junitTest);
        }

        outputCapture.assertContains("Running Test1\n", "Running Test2\n");
        outputCapture.assertDoseNotContain("Running Test3\n");
    }
}
