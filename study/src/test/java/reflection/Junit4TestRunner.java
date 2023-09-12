package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit4Test> clazz = Junit4Test.class;
        final Constructor<Junit4Test> constructor = clazz.getConstructor();
        final Junit4Test junit4Test = constructor.newInstance();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        for (Method method : clazz.getMethods()) {
            if (method.getDeclaredAnnotation(MyTest.class) != null) {
                method.invoke(junit4Test);
            }
        }
    }
}
