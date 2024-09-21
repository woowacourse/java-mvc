package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();

        List<String> methodNames = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                methodNames.add(method.getName());
                method.invoke(junit4Test);
            }
        }

        assertThat(methodNames).containsExactlyInAnyOrder("one", "two");
    }
}
