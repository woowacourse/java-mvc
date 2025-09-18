package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        // 클래스 객체 생성
        Class<Junit3Test> clazz = Junit3Test.class;
        // 인스턴스 생성
        Object testInstance = clazz.getDeclaredConstructor().newInstance();

        // 클래스 객체로부터 메서드 정보 추출
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                // 메서드 실행 (invoke(instance))
                method.invoke(testInstance);
            }
        }
    }
}
