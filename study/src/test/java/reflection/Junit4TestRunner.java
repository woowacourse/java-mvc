package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        List<String> invoked = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(clazz.getDeclaredConstructor().newInstance());
                invoked.add(method.getName());
            }
        }

        assertThat(invoked).containsExactlyInAnyOrder("one", "two");
    }
}
