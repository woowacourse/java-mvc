package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    private OutputStream captor;

    @BeforeEach
    void setUp() {
        captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));
    }

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = new Junit3Test();
        Method methods[] = clazz.getDeclaredMethods();

        for (Method m: methods) {
            if (m.getName().startsWith("test")) {
                m.invoke(junit3Test);
            }
        }

        String output = captor.toString().trim();

        assertThat(output)
            .contains("Running Test1")
            .contains("Running Test2")
            .doesNotContain("Running Test3");
    }
}
