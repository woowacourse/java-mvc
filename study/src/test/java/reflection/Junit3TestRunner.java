package reflection;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test junit3Test = clazz.newInstance();

        Method firstMethod = clazz.getMethod("test1");
        Method secondMethod = clazz.getMethod("test2");

        firstMethod.invoke(junit3Test);
        secondMethod.invoke(junit3Test);
    }

}
