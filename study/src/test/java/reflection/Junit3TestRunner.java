package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Nonnull
    private Method[] getMethods(Class<Junit3Test> clazz) {
        return clazz.getDeclaredMethods();
    }

    @Test
    void run() throws Exception {

        Predicate<Method> startWithTest = method -> method.getName().startsWith("test");
        List<Method> methodList = Arrays.stream(getMethods(Junit3Test.class))
                .filter(startWithTest)
                .toList();

        Junit3Test junit3Test = new Junit3Test();
        for (Method method : methodList) {
            method.invoke(junit3Test);
        }
    }
}
