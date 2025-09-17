package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test instance = new Junit4Test();

        // 클래스객체.getMethods()
        // - 클래스의 모든 메서드를 가져온다.
        Method[] methods = clazz.getMethods();

        // 메서드객체.isAnnotationPresent()
        // - 메서드가 특정 애노테이션을 가지고 있는지 확인한다.
        List<Method> testMethod = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .toList();

        for(Method method : testMethod) {
            // 메서드객체.invoke(객체인스턴스, 파라미터...)
            // - 메서드를 실행한다.
            // - 첫번째 인자는 메서드를 실행할 객체 인스턴스라는 점 유의하자.
            method.invoke(instance);
        }
    }
}
