package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] methods = clazz.getMethods();
        Junit4Test junit4Test = new Junit4Test();

        for (Method method : methods) {
            if (method.getAnnotation(MyTest.class) != null) {
                method.invoke(junit4Test);
            }
        }
    }
}
