package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Constructor<Junit4Test> constructor = clazz.getConstructor();
        Junit4Test junit4Test = constructor.newInstance();

        int invokedCount = 0;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(MyTest.class)) {
                declaredMethod.invoke(junit4Test);
                invokedCount++;
            }
        }

        assertThat(invokedCount).isEqualTo(2);
    }
}
