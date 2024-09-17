package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = new Junit3Test();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(c -> c.getName().startsWith("test"))
                .toList();

        List<String> invokedMethods = new ArrayList<>();

        for (Method method : methods) {
            method.invoke(junit3Test);
            invokedMethods.add(method.getName());
        }

        assertThat(invokedMethods).containsExactly("test1", "test2");
    }
}
