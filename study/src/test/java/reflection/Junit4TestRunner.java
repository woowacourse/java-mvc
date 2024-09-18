package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Constructor<Junit4Test> constructor = clazz.getConstructor();
        Junit4Test junit4Test = constructor.newInstance();

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            MyTest annotation = declaredMethod.getAnnotation(MyTest.class);
            if (annotation != null) {
                declaredMethod.invoke(junit4Test);
            }
        }
    }
}
