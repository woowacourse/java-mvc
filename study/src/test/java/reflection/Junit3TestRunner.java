package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        final Junit3Test junit3Test = clazz.newInstance();
        final Method[] declaredMethods = clazz.getDeclaredMethods();

        for (Method m:declaredMethods) {
            if(m.getName().startsWith("test")){
                m.invoke(junit3Test);
            }
        }
    }
}
