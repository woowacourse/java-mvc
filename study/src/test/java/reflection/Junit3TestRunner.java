package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        final Junit3Test junit3Test = clazz.getConstructor().newInstance();
        final Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            final String methodName = method.getName();
            if (methodName.startsWith("test")) {
                method.invoke(junit3Test);
            }
        }
    }
}
