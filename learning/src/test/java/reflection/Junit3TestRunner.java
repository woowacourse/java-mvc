package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class Junit3TestRunner extends JunitOutput {

    @Test
    void run() throws Exception {
        // given
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test test = clazz.getConstructor().newInstance();

        // when
        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();
            if (methodName.startsWith("test")) {
                method.invoke(test);
            }
        }

        // then
        String output = captor.toString().trim();
        assertThat(output)
                .contains(
                        "Running Test1",
                        "Running Test2")
                .doesNotContain("Running Test3");
    }
}
