package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner extends JunitTest {

    @Test
    void run() throws Exception {
        // given
        final Class<Junit3Test> clazz = Junit3Test.class;
        final Junit3Test junit3TestInstance = clazz.getDeclaredConstructor().newInstance();
        final Method[] declaredMethods = clazz.getDeclaredMethods();

        // when
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        for (final Method declaredMethod : declaredMethods) {
            declaredMethod.invoke(junit3TestInstance);
        }

        // then
        assertThat(out.toString()).isEqualTo(String.join(System.lineSeparator(),
                "Running Test1",
                "Running Test2",
                "Running Test3",
                ""
        ));
    }
}
