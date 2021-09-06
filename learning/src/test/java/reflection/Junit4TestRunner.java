package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = new Junit4Test();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method m : methods) {
            MyTest annotation = m.getDeclaredAnnotation(MyTest.class);

            if (annotation != null) {
                m.invoke(junit4Test);
            }
        }
    }
}
