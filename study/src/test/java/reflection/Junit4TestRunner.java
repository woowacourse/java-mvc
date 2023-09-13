package reflection;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        final Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            final int modifiers = method.getModifiers();
            final Annotation[] declaredAnnotations = method.getDeclaredAnnotations();

            if (Modifier.isPublic(modifiers) && isContainMyTest(declaredAnnotations)) {
                method.invoke(clazz.newInstance());
            }
        }
    }

    private static boolean isContainMyTest(final Annotation[] declaredAnnotations) {
        return Arrays.stream(declaredAnnotations)
                     .anyMatch(annotation -> annotation instanceof MyTest);
    }
}
