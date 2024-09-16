package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // given
        Junit4Test obj = new Junit4Test();
        Class<Junit4Test> clazz = Junit4Test.class;

        // when
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(obj);
            }
        }
        String actual = outputStream.toString();

        // then
        assertThat(actual).contains("Running Test1", "Running Test2");
    }
}
