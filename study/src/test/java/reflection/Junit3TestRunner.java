package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> isMethodNameStartingWith(method, "test"))
                .forEach(method -> invokeWithExceptionHandling(method, junit3Test));
    }

    private boolean isMethodNameStartingWith(Method method, String prefix) {
        return method.getName().startsWith(prefix);
    }

    private void invokeWithExceptionHandling(Method method, Object instance) {
        try {
            method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException("메소드 실행 중에 예외가 발생하였습니다: " + method.getName(), e);
        }
    }
}
