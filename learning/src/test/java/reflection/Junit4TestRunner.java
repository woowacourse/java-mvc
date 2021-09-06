package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        final Constructor<Junit4Test> declaredConstructor = clazz.getDeclaredConstructor();
        final Junit4Test junit4Test = declaredConstructor.newInstance();

        final Reflections reflections = new Reflections(clazz, new MethodAnnotationsScanner());
        final Set<Method> methodsAnnotatedWithMyTest = reflections.getMethodsAnnotatedWith(MyTest.class);

        for (Method method : methodsAnnotatedWithMyTest) {
            method.invoke(junit4Test);
        }
    }
}
