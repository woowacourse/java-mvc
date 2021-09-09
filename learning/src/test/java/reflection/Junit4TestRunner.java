package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner extends JunitOutput {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(junit4Test);
            }
        }

        String output = captor.toString().trim();

        // then
        assertThat(output).contains("Running Test1");
        assertThat(output).contains("Running Test2");
        assertThat(output).doesNotContain("Running Test3");
    }
}
