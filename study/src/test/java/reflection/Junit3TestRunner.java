package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    ByteArrayOutputStream outputStream;
    PrintStream out;

    @BeforeEach
    void setUp() {
        out = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("메서드 이릅이 test로 시작하는 메서드 실행")
    void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;

        final Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(clazz.newInstance());
            }
        }

        System.setOut(out);
        assertThat(outputStream.toString()).contains("Running Test3-1", "Running Test3-2")
                .doesNotContain("Running Test3-3");

    }
}
