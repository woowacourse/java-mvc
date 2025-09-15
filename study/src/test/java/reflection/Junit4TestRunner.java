package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        for (Method method : clazz.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof MyTest) {
                    Junit4Test target = clazz.getDeclaredConstructor().newInstance();
                    method.invoke(target);
                }
            }
        }
    }
}
