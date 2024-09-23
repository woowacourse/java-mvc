package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Constructor<Junit3Test> constructor = clazz.getConstructor();
        Junit3Test junit3Test = constructor.newInstance();

        int invokedCount = 0;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().startsWith("test")) {
                declaredMethod.invoke(junit3Test);
                invokedCount++;
            }
        }

        assertThat(invokedCount).isEqualTo(2);
    }
}
