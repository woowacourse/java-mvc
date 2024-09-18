package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        try {
            for (Method method : clazz.getMethods()) {
                if (method.getName().startsWith("test")) {
                    method.invoke(clazz.getDeclaredConstructor().newInstance());
                }
            }
        } finally {
            System.setOut(originalOut);
        }

        assertThat(outputStream.toString()).isEqualTo("Running Test1\nRunning Test2\n");
    }
}
