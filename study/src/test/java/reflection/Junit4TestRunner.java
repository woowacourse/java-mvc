package reflection;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test instance = Mockito.mock(Junit4Test.class);

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(instance);
            }
        }

        assertAll(
                () -> Mockito.verify(instance, times(1)).one(),
                () -> Mockito.verify(instance, times(1)).two(),
                () -> Mockito.verify(instance, never()).testThree()
        );
    }
}
