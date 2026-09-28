package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        List<String> invoked = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(clazz.getDeclaredConstructor().newInstance());
                invoked.add(method.getName());
            }
        }

        assertThat(invoked).containsExactlyInAnyOrder("test1", "test2");
    }
}
