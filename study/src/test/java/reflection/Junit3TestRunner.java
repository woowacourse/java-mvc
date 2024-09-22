package reflection;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = spy(clazz.getDeclaredConstructor().newInstance());

        clazz.getMethod("test1").invoke(junit3Test);

        verify(junit3Test, times(1)).test1();
    }
}
