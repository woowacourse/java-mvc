package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        for (Method method : clazz.getDeclaredMethods()) { // JUnit3Test 클래스 안에 직접 선언된 메서드만 전부 가져옴
            if (method.getName().startsWith("test")) {
                Junit3Test test = new Junit3Test(); // 객체를 하나만 쓰면 테스트에 따라 상태가 변할 수 있어서, 반복문 안에서 생성
                method.invoke(test);
            }
        }
    }
}
