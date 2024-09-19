package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test instance = clazz.getConstructor().newInstance();

        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(instance);
            }
        }

        String expect = "Running Test1" + System.lineSeparator() + "Running Test2" + System.lineSeparator();
        assertThat(outputStream).hasToString(expect);
    }
}
