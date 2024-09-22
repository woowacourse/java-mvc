package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @DisplayName("Junit3Test 클래스에서 test로 시작하는 메서드를 모두 실행한다")
    @Test
    void runJunit3TestMethods() {
        Class<Junit3Test> clazz = Junit3Test.class;

        Arrays.stream(clazz.getMethods()) // Junit3Test에서 test로 시작하는 메소드 실행
                .filter(this::isTestMethod)
                .forEach(this::invokeJunit3TestMethod);
    }

    @DisplayName("클래스에서 test로 시작하는 메서드를 모두 실행한다")
    @Test
    void runGeneralTestMethods() {
        Class<Junit3Test> clazz = Junit3Test.class;

        Arrays.stream(clazz.getMethods())
                .filter(this::isTestMethod)
                .forEach(method -> invokeTestMethod(clazz, method));
    }

    private boolean isTestMethod(Method method) {
        String name = method.getName();
        return name.startsWith("test");
    }

    private void invokeJunit3TestMethod(Method method) {
        try {
            Junit3Test instance = new Junit3Test(); // 각 테스트 메서드마다 새로운 인스턴스 생성해 상태 공유 문제 방지
            method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void invokeTestMethod(Class<T> clazz, Method method) {
        try {
            T instance = clazz.getConstructor()
                    .newInstance();  // 후에 여러 클래스 공통으로 처리할 수 있게 하기 위해, 생성자 호출해 인스턴스 만드는 것도 리플렉션 사용함
            method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
