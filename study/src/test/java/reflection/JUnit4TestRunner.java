package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class JUnit4TestRunner {

    @Test
    void run() throws Exception {
        Class<JUnit4Test> clazz = JUnit4Test.class;

        JUnit4Test test = new JUnit4Test();
        Method[] declaredMethods = clazz.getDeclaredMethods();

        int invokedMethodCount = 0;
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(test);
                invokedMethodCount++;
            }
        }
        assertThat(invokedMethodCount).isEqualTo(2);
    }
}
