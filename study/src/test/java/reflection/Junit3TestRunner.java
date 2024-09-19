package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // given
        Junit3Test obj = new Junit3Test();
        Class<Junit3Test> classObj = Junit3Test.class;

        // when
        List<Method> targetMethods = Arrays.stream(classObj.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .toList();

        for(Method method : targetMethods) {
            method.invoke(obj);
        }

        String actual = outputStream.toString();

        // then
        assertThat(actual).contains("Running Test1", "Running Test2");
    }
}
