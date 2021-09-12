package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        //given
        final Class<Junit4Test> clazz = Junit4Test.class;
        final Annotation[] annotations = clazz.getAnnotations();
        final Constructor<?> constructor = clazz.getConstructor();
        final Object instance = constructor.newInstance();
        final Method[] methods = clazz.getDeclaredMethods();

        //when
        int answer = 0;
        for (Method method : methods) {
            answer += invokeMethod(method, instance);
        }

        assertThat(answer).isEqualTo(2);
    }

    private int invokeMethod(Method method, Object object)
        throws IllegalAccessException, InvocationTargetException {
        final Annotation annotationName = method.getAnnotation(MyTest.class);

        if (annotationName instanceof MyTest) {
            method.invoke(object);
            return 1;
        }

        return 0;
    }
}
