package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Method;
import java.util.Set;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // sol1
        System.out.println("---With Reflection Library---");

        Reflections reflection = new Reflections("reflection", Scanners.MethodsAnnotated);

        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();

        Set<Method> methods = reflection.getMethodsAnnotatedWith(MyTest.class);
        for (Method method : methods) {
            method.invoke(junit4Test);
        }

        // sol2
        System.out.println("\n---With Class Api---");

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(junit4Test);
            }
        }
    }
}
