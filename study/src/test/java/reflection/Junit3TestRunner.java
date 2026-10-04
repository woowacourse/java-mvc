package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // 생성자 가져온 후 새로 생성
        Junit3Test instance = clazz.getDeclaredConstructor().newInstance();

        // 메소드 가져온 후 시작
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(instance);
            }
        }
    }
}
