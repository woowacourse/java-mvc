package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;
        final Constructor<Junit3Test> constructor = clazz.getDeclaredConstructor();
        final Junit3Test junit3Test = constructor.newInstance();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        for (Method method : clazz.getMethods()) {
            final String name = method.getName();
            if (name.startsWith("test")) {
                method.invoke(junit3Test);
            }
        }
    }
}
