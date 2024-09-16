package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                // 메소드가 private일 경우 접근 가능하도록 설정
                method.setAccessible(true);
                // Junit3Test의 인스턴스를 생성
                Junit3Test instance = clazz.getDeclaredConstructor().newInstance();
                // 메소드 실행
                method.invoke(instance);
            }
        }
    }
}
