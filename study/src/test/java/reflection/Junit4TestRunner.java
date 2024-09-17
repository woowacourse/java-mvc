package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = new Junit4Test();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(c -> c.isAnnotationPresent(MyTest.class))
                .toList();

        List<String> invokedMethods = new ArrayList<>();

        for (Method method : methods) {
            method.invoke(junit4Test);
            invokedMethods.add(method.getName());
        }

        assertThat(invokedMethods).containsExactly("one", "two");
    }
}
