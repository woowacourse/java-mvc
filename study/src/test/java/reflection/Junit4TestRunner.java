package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                executeMethod(clazz, method.getName());
            }
        }
    }

    private void executeMethod(final Class<Junit4Test> clazz, final String methodName)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Junit4Test junit4Test = new Junit4Test();
        final Method testMethod = clazz.getMethod(methodName);

        testMethod.invoke(junit4Test);
    }
}
