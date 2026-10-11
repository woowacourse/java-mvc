package reflection;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test instance = spy(new Junit3Test());

        Method[] declaredMethods = clazz.getDeclaredMethods();

        for (Method declaredMethod : declaredMethods) {
            String name = declaredMethod.getName();
            if (name.startsWith("test")) {
                declaredMethod.invoke(instance);
            }
        }

        verify(instance).test1();
        verify(instance).test2();
        verify(instance, never()).three();
    }
}
