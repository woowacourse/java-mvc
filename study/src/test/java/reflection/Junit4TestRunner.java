package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import java.lang.reflect.Method;
import java.util.Set;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getConstructor().newInstance();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Reflections reflections = new Reflections("reflection");
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(MyTest.class);
        for (Method method : methodsAnnotatedWith) {
            method.invoke(junit4Test, (Object[]) null);
        }
    }
}
