package reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // Junit4Test의 인스턴스를 생성
        Junit4Test instance = new Junit4Test();

        // 클래스의 모든 메서드를 조회
        Method[] methods = clazz.getDeclaredMethods();

        // @MyTest 애노테이션이 붙은 메서드만 실행
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    // static 메서드인 경우
                    method.invoke(null);  // null을 인자로 사용하여 호출
                } else {
                    // 인스턴스 메서드인 경우
                    method.invoke(instance);  // 인스턴스에서 메서드 실행
                }
            }
        }
    }
}
