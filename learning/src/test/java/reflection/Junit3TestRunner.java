package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner extends JunitOutput {

    @Test
    void run() throws Exception {

        //given
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();
        Method[] methods = clazz.getDeclaredMethods();

        // when
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(junit3Test);
            }
        }
        String output = captor.toString().trim();

        // then
        assertThat(output).contains("Running Test1");
        assertThat(output).contains("Running Test2");
        assertThat(output).doesNotContain("Running Test3");
    }

    @Test
    void run2() throws Exception {

        //given
        Class<?> clazz = Class.forName("reflection.Junit3Test");

        Object junit3Test = clazz.getDeclaredConstructor().newInstance();
        Method[] methods = clazz.getDeclaredMethods();

        // when
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(junit3Test);
            }
        }
        String output = captor.toString().trim();

        // then
        assertThat(output).contains("Running Test1");
        assertThat(output).contains("Running Test2");
        assertThat(output).doesNotContain("Running Test3");
    }
}
