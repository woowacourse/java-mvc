package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        for (Method method : clazz.getMethods()) {
            MyTest myTestClass = method.getAnnotation(MyTest.class);
            if (myTestClass != null && myTestClass.equals(MyTest.class)) {
                method.invoke(new Junit4Test(), new Object[]{});
            }
        }
    }
}
