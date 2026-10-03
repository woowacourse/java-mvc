package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        // Junit4Test에서 @MyTest 애노테이션이 있는 메서드 실행
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4TestInstance = clazz.getDeclaredConstructor().newInstance();
        TargetAnnotation testAnnotation = new TargetAnnotation("MyTest");

        for (Method method : clazz.getDeclaredMethods()) {
            if (testAnnotation.included(method)) {
                method.invoke(junit4TestInstance);
            }
        }
    }
}
