package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    @DisplayName("Junit3Test에서 test로 시작하는 메소드 실행 성공")
    void run() throws Exception {
        // given
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // When
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test testObj = clazz.getConstructor()
                .newInstance();

        List<Method> methods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .toList();
        for (Method method : methods) {
            method.invoke(testObj);
        }

        // Then
        assertThat(outContent.toString())
                .contains("Running Test1", "Running Test2")
                .doesNotContain("Running Test3");

        System.setOut(originalOut);
    }
}
