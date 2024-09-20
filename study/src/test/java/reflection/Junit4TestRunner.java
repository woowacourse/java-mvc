package reflection;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        // Junit4Test 인스턴스 생성
        Junit4Test instance = clazz.getDeclaredConstructor().newInstance();

        // 클래스의 모든 메소드 가져오기
        Method[] methods = clazz.getDeclaredMethods();

        // @MyTest 애노테이션이 있는 메소드 실행
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(instance);  // 메소드 실행
            }
        }
    }
}
