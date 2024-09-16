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

class Junit4TestRunner {
    private static final String ONE_METHOD_OUTPUT = "Running Test1";
    private static final String TWO_METHOD_OUTPUT = "Running Test2";
    private static final String TEST_THREE_METHOD_OUTPUT = "Running Test3";

    @DisplayName("Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행")
    @Test
    void run() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            executeTestMethodHasMyTestAnnotation();
        } finally {
            System.setOut(originalOutput);
        }

        String output = outputStream.toString();
        assertAll(
                () -> assertThat(output).contains(ONE_METHOD_OUTPUT),
                () -> assertThat(output).contains(TWO_METHOD_OUTPUT),
                () -> assertThat(output).doesNotContain(TEST_THREE_METHOD_OUTPUT)
        );
    }

    private void executeTestMethodHasMyTestAnnotation() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test instance = clazz.getDeclaredConstructor().newInstance();
        List<Method> methods = List.of(clazz.getDeclaredMethods());

        methods.stream()
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> invokeMethod(method, instance));
    }

    private void invokeMethod(Method method, Junit4Test instance) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
