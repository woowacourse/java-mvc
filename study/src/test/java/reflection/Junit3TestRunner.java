package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        final Constructor<Junit3Test> declaredConstructor = clazz.getDeclaredConstructor(); // 생성자 받아와서
        final Junit3Test junit3Test = declaredConstructor.newInstance(); // 객체 만들기

        for (final Method method : clazz.getMethods()) {
            if(method.getName().startsWith("test")){
                method.invoke(junit3Test);
            }
        }
    }
}
