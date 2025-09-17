package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행

        // 인스턴스 생성
        Object testInstance = clazz.getDeclaredConstructor().newInstance();

        // 메서드 순회
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) { // @MyTest 애노테이션이 있눈 메서드 탐색
                method.invoke(testInstance); // 해당 메서드 실행
            }
        }
    }
}
