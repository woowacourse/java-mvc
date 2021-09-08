package reflection;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Junit3TestRunner {

    @Test
    void run() throws Exception {

        // given
        final ByteArrayOutputStream captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));

        Class<Junit3Test> clazz = Junit3Test.class;
        final List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                                                    .filter(method -> method.getName().startsWith("test"))
                                                    .collect(Collectors.toList());

        final Junit3Test junit3Test = clazz.getConstructor().newInstance();

        // when
        for (Method method : methods) {
            method.invoke(junit3Test);
        }

        // then
        assertThat(captor.toString().trim()).isEqualTo("Running Test1\nRunning Test2");
    }
}
