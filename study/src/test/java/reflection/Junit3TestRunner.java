package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Method[] methods = clazz.getMethods();
        Constructor<Junit3Test> constructor = clazz.getConstructor();
        Junit3Test obj = constructor.newInstance();

        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(obj);
            }
        }
    }
}
