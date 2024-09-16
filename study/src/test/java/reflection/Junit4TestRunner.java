package reflection;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit4Test> clazz = Junit4Test.class;

        final Junit4Test instance = Mockito.spy(clazz.getDeclaredConstructor().newInstance());
        Arrays.stream(clazz.getDeclaredMethods()).filter(method ->
                Arrays.stream(method.getAnnotations()).anyMatch(annotation -> annotation.annotationType() == MyTest.class))
                .forEach(method -> {
                    try {
                        method.invoke(instance);
                    } catch (final IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });

        Mockito.verify(instance, Mockito.times(1)).one();
        Mockito.verify(instance, Mockito.times(1)).two();
        Mockito.verify(instance, Mockito.times(0)).testThree();
    }
}
