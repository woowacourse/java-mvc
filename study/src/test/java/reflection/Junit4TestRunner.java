package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        // 클래스 객체로 모든 메서드를 가져온다.
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = new Junit4Test();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        // reflection을 통해 동적으로 특정 어노테이션이 붙은 메서드를 실행한다.

        // 메서드에 특정 어노테이션이 붙었는지 확인한다.
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(junit4Test);
            }
        }
    }
}
