package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;

        System.setOut(printStream);

        try {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(MyTest.class)) {
                    method.invoke(clazz.getConstructor().newInstance());
                }
            }
        } finally {
            System.setOut(originalOut);
        }

        assertThat(outputStream.toString()).isEqualTo("Running Test1\nRunning Test2\n");
    }
}
