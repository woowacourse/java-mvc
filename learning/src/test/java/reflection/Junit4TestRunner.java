package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Method;
import java.util.Set;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        //given
        final Junit4Test junit4Test = new Junit4Test();
        final Reflections reflections = new Reflections("reflection", new MethodAnnotationsScanner(), new SubTypesScanner());
        //when
        final Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(MyTest.class);
        //then
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        for (Method method : methodsAnnotatedWith) {
            method.invoke(junit4Test);
        }
    }
}
