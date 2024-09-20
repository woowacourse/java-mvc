package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() {
        final Class<Junit3Test> clazz = Junit3Test.class;
        Stream.of(clazz.getMethods())
                .filter(this::hasPrefix)
                .forEach(method -> invokeMethod(clazz, method));
    }

    @Test
    void assert_junit3_runner() {
        // given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // when
        run();

        // then
        assertThat(outputStream)
                .hasToString("Running Test1" + System.lineSeparator() + "Running Test2" + System.lineSeparator());
    }

    private boolean hasPrefix(final Method method) {
        return method.getName().startsWith("test");
    }

    private void invokeMethod(final Class<?> clazz, final Method method) {
        try {
            final Object instance = clazz.getDeclaredConstructor().newInstance();
            method.invoke(instance);
        } catch (final NoSuchMethodException | InvocationTargetException | InstantiationException |
                       IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
