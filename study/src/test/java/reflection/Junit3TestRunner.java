package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
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
        Method test1 = classObj.getDeclaredMethod("test1");
        Method test2 = classObj.getDeclaredMethod("test2");

        test1.invoke(obj); // obj.test1();
        test2.invoke(obj); // obj.test2();

        String actual = outputStream.toString();

        // then
        assertThat(actual).contains("Running Test1", "Running Test2");
    }
}
