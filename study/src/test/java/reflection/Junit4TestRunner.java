package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Reflections reflections = new Reflections(clazz);

        for (Method method : reflections.getMethodsAnnotatedWith(MyTest.class)) {
            method.invoke(clazz);
        }
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
    }
}
