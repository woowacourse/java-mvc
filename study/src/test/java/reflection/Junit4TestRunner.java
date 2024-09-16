package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    ByteArrayOutputStream outputStream;
    PrintStream out;

    @BeforeEach
    void setUp() {
        out = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }


    @Test
    @DisplayName("Junit4Test에서 @MyTest 애노테이션이 있는 메서드 실행")
    void run() throws Exception {
        final Class<Junit4Test> clazz = Junit4Test.class;

        final Method[] methods = clazz.getMethods();

        for (final Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(clazz.newInstance());
            }
        }

        System.setOut(out);
        assertThat(outputStream.toString()).contains("Running Test4-1", "Running Test4-2")
                .doesNotContain("Running Test4-3");

    }
}
