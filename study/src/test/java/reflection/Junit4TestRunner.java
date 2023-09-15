package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final Method[] methods = clazz.getMethods();
        final Junit4Test junit4Test = clazz.getConstructor().newInstance();
        for (Method method : methods) {
            final MyTest testAnnotation = method.getAnnotation(MyTest.class);
            if (testAnnotation != null) {
                method.invoke(junit4Test);
            }
        }
        // Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
    }
}
