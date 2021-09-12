package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();
            Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();
            if (methodName.startsWith("test")) {
                System.out.println("실행한 메서드: " + clazz.getSimpleName() + "#" + methodName);
                method.invoke(junit3Test);
            }
        }
    }
}
