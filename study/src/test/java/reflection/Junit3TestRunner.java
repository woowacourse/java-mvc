package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // Junit3Test 클래스의 모든 메소드를 가져옵니다.
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            String methodName = method.getName();
            // 메소드 이름이 "test"로 시작하는 경우 실행합니다.
            if (methodName.startsWith("test")) {
                method.invoke(clazz.newInstance());
            }
        }
    }
}
