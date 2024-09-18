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
        Junit4Test junit4Test = new Junit4Test();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;

        System.setOut(printStream);

        try {
            for (Method method : clazz.getMethods()) {
                if (method.getDeclaredAnnotation(MyTest.class) != null) {
                    method.invoke(junit4Test);
                }
            }
        } finally {
            System.setOut(originalOut);
        }

        assertThat(outputStream.toString()).isEqualTo("Running Test1\nRunning Test2\n");
    }
}
