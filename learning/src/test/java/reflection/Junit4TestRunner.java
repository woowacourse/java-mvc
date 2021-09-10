package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getConstructor().newInstance();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            MyTest myTest = method.getDeclaredAnnotation(MyTest.class);

            if (myTest != null) {
                method.invoke(junit4Test);
            }
        }
    }
}
