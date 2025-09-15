package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Objects;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test instance = clazz.getDeclaredConstructor().newInstance();
        instance.test1();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
    }
}
