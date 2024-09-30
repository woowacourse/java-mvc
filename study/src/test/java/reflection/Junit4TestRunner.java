package reflection;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = spy(clazz.getDeclaredConstructor().newInstance());

        Method[] declaredMethods = clazz.getDeclaredMethods();
        Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> invoke(method, junit4Test));

        verify(junit4Test, times(1)).one();
        verify(junit4Test, times(1)).two();
        verify(junit4Test, times(0)).testThree();
    }

    private void invoke(Method method, Junit4Test junit4Test) {
        try {
            method.invoke(junit4Test);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
