package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> invokeWithExceptionHandling(method, junit4Test));
    }

    private void invokeWithExceptionHandling(Method method, Object instance) {
        try {
            method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException("메소드 실행 중에 예외가 발생하였습니다: " + method.getName(), e);
        }
    }
}
