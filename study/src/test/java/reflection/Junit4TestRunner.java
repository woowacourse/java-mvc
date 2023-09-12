package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit4TestRunner extends JunitTest {

    @Test
    void run() throws Exception {
        // given
        final Class<Junit4Test> clazz = Junit4Test.class;
        final Junit4Test junit4TestInstance = clazz.getDeclaredConstructor().newInstance();
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        final List<Method> myTestAnnotatedMethods = Arrays.stream(declaredMethods)
                .filter(it -> it.isAnnotationPresent(MyTest.class))
                .collect(Collectors.toUnmodifiableList());

        // when
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        for (final Method myTestAnnotatedMethod : myTestAnnotatedMethods) {
            myTestAnnotatedMethod.invoke(junit4TestInstance);
        }

        // then
        assertThat(out.toString()).isEqualTo(String.join(System.lineSeparator(),
                "Running Test1",
                "Running Test2",
                ""
        ));
    }
}
