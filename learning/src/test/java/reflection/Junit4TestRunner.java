package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Objects;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        final Junit4Test junit4Test = clazz.getConstructor().newInstance();

        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            final MyTest myTestAnnotation = method.getAnnotation(MyTest.class);
            if (!Objects.isNull(myTestAnnotation)) {
                method.invoke(junit4Test);
            }
        }
    }
}
