package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = new Junit4Test();

        int count = 0;
        for (Method method : clazz.getMethods()) {
            if (method.getDeclaredAnnotation(MyTest.class) != null) {
                method.invoke(junit4Test);
                count++;
            }
        }

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        assertThat(count).isEqualTo(2);
    }
}
