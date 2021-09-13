package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        // given
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] methods = clazz.getDeclaredMethods();
        Constructor<Junit4Test> constructor = clazz.getDeclaredConstructor();
        Junit4Test junit4Test = constructor.newInstance();

        // when, then
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                execute(method, junit4Test);
            }
        }
    }

    private void execute(Method method, Junit4Test junit4Test) {
        try {
            method.invoke(junit4Test);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }
}
