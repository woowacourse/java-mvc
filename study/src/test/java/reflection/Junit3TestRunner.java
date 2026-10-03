package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        // Junit3Test.class = 클래스 정보
        // new Junit3Test() = 실제 객체 생성
        Class<Junit3Test> clazz = Junit3Test.class;

        // 테스트 객체 생성
        Junit3Test testObject = clazz.getDeclaredConstructor().newInstance();

        // 메서드 목록 가져오기
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(testObject);
            }
        }
    }
}
