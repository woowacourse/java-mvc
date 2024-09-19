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
        Junit3Test instance = clazz.getConstructor().newInstance();

        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(instance);
            }
        }

        String expect = "Running Test1" + System.lineSeparator() + "Running Test2" + System.lineSeparator();
        assertThat(outputStream).hasToString(expect);
    }
}
