package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    // TODO Junit3Test에서 test로 시작하는 메소드 실행
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test instance = new Junit3Test();

        // 클래스객체.getMethods()
        // - 클래스의 모든 메서드를 가져온다.
        Method[] methods = clazz.getMethods();

        // 메서드객체.getName()
        // - 메서드의 이름을 가져온다.
        List<Method> testMethod = Arrays.stream(methods)
                .filter(method -> method.getName().startsWith("test"))
                .toList();

        for(Method method : testMethod) {
            // 메서드객체.invoke(객체인스턴스, 파라미터...)
            // - 메서드를 실행한다.
            // - 첫번째 인자는 메서드를 실행할 객체 인스턴스라는 점 유의하자.
            method.invoke(instance);
        }
    }

    /*
    ** 추가 학습 포인트
    - 클래스객체.getMethod(메서드명, 파라미터타입...)
    - 클래스객체.getDeclaredMethod(메서드명, 파라미터타입...)
     */
}
