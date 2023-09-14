package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        final Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            final int modifiers = method.getModifiers();

            if (isStartsWithTest(method) && Modifier.isPublic(modifiers)) {
                method.invoke(clazz.newInstance());
            }
        }
    }

    private static boolean isStartsWithTest(final Method method) {
        return method.getName().startsWith("test");
    }
}
