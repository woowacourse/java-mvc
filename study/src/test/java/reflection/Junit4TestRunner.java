package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // 리플렉션으로 Junit4Test 클래스의 인스턴스를 새로 만듬
        Object instance = clazz.getDeclaredConstructor().newInstance();

        // Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(instance);
            }
        }
    }
}
