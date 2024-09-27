package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(junit3Test);
            }
        }

        assertThat(outputStream.toString())
                .contains("Running Test1", "Running Test2")
                .doesNotContain("Running Test3");
        System.setOut(System.out);
    }
}
