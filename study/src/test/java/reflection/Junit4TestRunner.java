package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        final List<Method> methods = Arrays.stream(clazz.getMethods())
                .filter(m -> m.isAnnotationPresent(MyTest.class))
                .toList();

        for (Method method : methods) {
            method.invoke(new Junit4Test());
        }
    }
}
