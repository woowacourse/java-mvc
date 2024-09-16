package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = new Junit3Test();

        int count = 0;
        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(junit3Test);
                count++;
            }
        }

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        assertThat(count).isEqualTo(2);
    }
}
