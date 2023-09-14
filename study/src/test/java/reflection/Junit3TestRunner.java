package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        final Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            runTestStartsWithTest(method, clazz);
        }
    }

    private void runTestStartsWithTest(final Method method, final Class<Junit3Test> clazz)
        throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        final Junit3Test junit3Test = clazz.getConstructor().newInstance();
        final String methodName = method.getName();
        if (methodName.startsWith("test")) {
            method.invoke(junit3Test);
        }
    }
}
