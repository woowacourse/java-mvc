package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        final Junit4Test instance = clazz.getDeclaredConstructor().newInstance();
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method: methods) {
            Annotation annotation = method.getDeclaredAnnotation(MyTest.class);
            if(annotation != null) {
                method.invoke(instance);
            }
        }
    }
}
