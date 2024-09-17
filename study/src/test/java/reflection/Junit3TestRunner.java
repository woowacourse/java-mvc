package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        List<Method> methods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                methods.add(method);
            }
        }

        Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();
        List<String> methodNames = new ArrayList<>();
        for (Method method : methods) {
            methodNames.add(method.getName());
            method.invoke(junit3Test);
        }

        assertThat(methodNames).containsExactlyInAnyOrder("test1", "test2");
    }
}
