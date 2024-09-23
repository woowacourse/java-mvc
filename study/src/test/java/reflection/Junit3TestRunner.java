package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // Junit3Test의 인스턴스를 생성
        Junit3Test instance = clazz.getDeclaredConstructor().newInstance();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                // 메소드 실행
                method.invoke(instance);
            }
        }
    }
}
