package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test testInstance = clazz.getDeclaredConstructor().newInstance();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Method test1 = clazz.getMethod("test1");
        Method test2 = clazz.getMethod("test2");

        test1.invoke(testInstance);
        test2.invoke(testInstance);
    }
}
