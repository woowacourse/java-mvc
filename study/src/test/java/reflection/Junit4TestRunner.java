package reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();

        List<String> methodNames = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                methodNames.add(method.getName());
                method.invoke(junit4Test);
            }
        }

        Assertions.assertThat(methodNames).containsExactlyInAnyOrder("one", "two");
    }
}
