package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // 리플렉션으로 Junit3Test 클래스의 인스턴스를 새로 만듬
        Object instance = clazz.getDeclaredConstructor().newInstance();

        // Junit3Test에서 test로 시작하는 메소드 실행
        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(instance);
            }
        }
    }
}
