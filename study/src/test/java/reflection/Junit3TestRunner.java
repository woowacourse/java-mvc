package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test junit3Test = clazz.getConstructor()
            .newInstance();

        Method test1 = clazz.getMethod("test1");
        test1.invoke(junit3Test);

        Method test2 = clazz.getMethod("test2");
        test2.invoke(junit3Test);

        Method test3 = clazz.getMethod("three");
        test3.invoke(junit3Test);
    }
}
