package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;
        final Constructor<Junit3Test> constructor = clazz.getDeclaredConstructor();
        final Junit3Test junit3Test = constructor.newInstance();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        for (final Method declaredMethod : declaredMethods) {
            declaredMethod.invoke(junit3Test);
        }
    }
}
