package reflection;

import java.lang.reflect.Method;
import java.util.Objects;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행

        Junit4Test instance = new Junit4Test();
        Class<Junit4Test> clazz = Junit4Test.class;

        for (Method method : clazz.getDeclaredMethods()) {
            MyTest annotation = method.getAnnotation(MyTest.class);

            if (Objects.nonNull(annotation)) {
                method.invoke(instance);
            }
        }
    }
}
