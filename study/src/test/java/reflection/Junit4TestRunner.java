package reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test spiedInstance = Mockito.spy(clazz.getDeclaredConstructor().newInstance());

        // Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> {
                    try {
                        method.invoke(spiedInstance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });

        Mockito.verify(spiedInstance, Mockito.times(1)).one();
        Mockito.verify(spiedInstance, Mockito.times(1)).two();
        Mockito.verify(spiedInstance, Mockito.never()).testThree();
    }
}
