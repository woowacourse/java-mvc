package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {

        Junit4Test junit4Test = new Junit4Test();

        for (Method method : getMethods(Junit4Test.class)) {
            MyTest annotation = method.getAnnotation(MyTest.class);
            if (annotation == null) {
                continue;
            }

            method.invoke(junit4Test);
        }
    }

    @Nonnull
    private List<Method> getMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .toList();
    }

}
