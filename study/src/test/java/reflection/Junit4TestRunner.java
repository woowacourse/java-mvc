package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        final Method[] methods = clazz.getMethods();
        final Junit4Test junit4Test = clazz.getConstructor().newInstance();

        for (final Method method : methods) {
            runAnnotatedMethods(method, junit4Test);
        }
    }

    private void runAnnotatedMethods(final Method method, final Junit4Test junit4Test)
        throws IllegalAccessException, InvocationTargetException {
        if (method.isAnnotationPresent(MyTest.class)) {
            method.invoke(junit4Test);
        }
    }
}
