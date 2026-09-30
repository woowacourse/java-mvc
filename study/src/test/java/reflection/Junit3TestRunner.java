package reflection;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();

        Object test = clazz.getDeclaredConstructor().newInstance();

        for (java.lang.reflect.Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(test);
            }
        }
    }
}
