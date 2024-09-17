package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test test = new Junit3Test();

        Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().startsWith("test"))
                .forEach(method -> invoke(method, test));
    }

    private void invoke(Method method, Junit3Test test) {
        try {
            method.invoke(test);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
