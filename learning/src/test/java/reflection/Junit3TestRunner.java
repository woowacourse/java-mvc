package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        //given
        final Class<Junit3Test> clazz = Junit3Test.class;
        final Constructor<?> constructor = clazz.getConstructor();
        final Object instance = constructor.newInstance();
        final Method[] methods = clazz.getDeclaredMethods();

        //when
        int answer = 0;
        for (Method method : methods) {
            answer += invokeMethod(method, instance);
        }

        //then
        assertThat(answer).isEqualTo(2);
    }

    private int invokeMethod(Method method, Object object)
        throws IllegalAccessException, InvocationTargetException {
        final String methodName = method.getName();
        if (methodName.startsWith("test")) {
            method.invoke(object);
            return 1;
        }
        return 0;
    }
}
