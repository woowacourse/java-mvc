package reflection;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test instance = Mockito.mock(Junit3Test.class);

        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(instance);
            }
        }

        assertAll(
                () -> Mockito.verify(instance, times(1)).test1(),
                () -> Mockito.verify(instance, times(1)).test2(),
                () -> Mockito.verify(instance, never()).three()
        );
    }
}
