package reflection;

import org.junit.jupiter.api.Test;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Junit4Test instance = new Junit4Test();

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(MyTest.class)) {
                    method.invoke(instance);
                }
            }
        }
    }
}
