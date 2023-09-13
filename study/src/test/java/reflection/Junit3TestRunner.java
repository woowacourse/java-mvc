package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        for (Method method : clazz.getDeclaredMethods()) {
            executeMethod(clazz, method.getName());
        }
    }

    private void executeMethod(final Class<Junit3Test> clazz, final String methodName)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Junit3Test junit3Test = new Junit3Test();
        final Method testMethod = clazz.getMethod(methodName);

        testMethod.invoke(junit3Test);
    }
}
