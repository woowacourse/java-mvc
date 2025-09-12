package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final Constructor<Junit4Test> declaredConstructor = clazz.getDeclaredConstructor();
        final Junit4Test junit4Test = declaredConstructor.newInstance();

        for (final Method method : clazz.getMethods()) {
            if(method.isAnnotationPresent(MyTest.class)){
                method.invoke(junit4Test);
            }
        }
    }
}
