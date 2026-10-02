package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Junit4Test instance = clazz.getDeclaredConstructor().newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(instance);
            }
            /*
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                // 다른 방법 1
                if (annotation.annotationType().isAssignableFrom(MyTest.class)) {
                    method.invoke(instance);
                }

                // 다른 방법 2
                annotation.annotationType() == MyTest.class

                // 다른 방법 3
                MyTest.class.isAssignableFrom(annotation.annotationType())
            }
            */
        }
    }
}
