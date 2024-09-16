package reflection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {
    private static final String TEST1_TEST_OUTPUT = "Running Test1";
    private static final String TEST2_TEST_OUTPUT = "Running Test2";
    private static final String THREE_TEST_OUTPUT = "Running Test3";

    @DisplayName("Junit3Test에서 test로 시작하는 메소드만 실행한다.")
    @Test
    void run() throws Exception {
        // given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // when
        try {
            executeTestMethodStartsWithTest();
        } finally {
            System.setOut(originalOut);
        }

        // then
        String output = outputStream.toString();

        assertAll(
                () -> assertThat(output).contains(TEST1_TEST_OUTPUT),
                () -> assertThat(output).contains(TEST2_TEST_OUTPUT),
                () -> assertThat(output).doesNotContain(THREE_TEST_OUTPUT)
        );
    }

    private void executeTestMethodStartsWithTest() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test instance = clazz.getDeclaredConstructor().newInstance();
        List<Method> methods = List.of(clazz.getDeclaredMethods());
        methods.stream()
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> invokeMethod(method, instance));
    }

    private void invokeMethod(Method method, Junit3Test instance) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
