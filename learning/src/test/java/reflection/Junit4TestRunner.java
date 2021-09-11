package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    private OutputStream captor;

    @BeforeEach
    void setUp() {
        captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));
    }

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = new Junit4Test();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method m : methods) {
            MyTest annotation = m.getDeclaredAnnotation(MyTest.class);

            if (annotation != null) {
                m.invoke(junit4Test);
            }
        }

        String output = captor.toString().trim();

        assertThat(output)
            .contains("Running Test1")
            .contains("Running Test2")
            .doesNotContain("Running Test3");
    }
}
