package reflection;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Junit4TestRunner {

    @Test
    void run() throws Exception {

        // given
        final ByteArrayOutputStream captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));

        Class<Junit4Test> clazz = Junit4Test.class;
        final List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                                           .filter(method -> method.isAnnotationPresent(MyTest.class))
                                           .collect(Collectors.toList());

        final Junit4Test junit4Test = clazz.getConstructor().newInstance();

        // when
        for (Method method : methods) {
            method.invoke(junit4Test);
        }

        // then
        assertThat(captor.toString().trim()).isEqualTo("Running Test1\nRunning Test2");
    }
}
