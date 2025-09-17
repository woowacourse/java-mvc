package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // Junit3Test에서 test로 시작하는 메소드 실행

        // 인스턴스 생성
        Object testInstance = clazz.getDeclaredConstructor().newInstance();

        // 메서드 순회
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) { // test로 시작하는 method 찾기
                method.invoke(testInstance); // 해당 메서드 실행
            }
        }

        // getDeclaredMethods()
        // 이 메서드는 해당 클래스 안에 “선언된” 모든 메서드를 배열로 반환
        // (private, protected, default, public 전부 포함)
        // 단, 상속받은 메서드는 포함하지 않음.

        // getMethods()와의 차이점
        // getMethods() → public 메서드만 가져옴 + 상속된 메서드도 포함
        // getDeclaredMethods() → 접근제한자 상관없이 현재 클래스에 정의된 메서드만 가져옴
    }
}
