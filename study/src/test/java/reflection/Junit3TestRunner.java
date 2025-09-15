package reflection;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // Junit3Test에서 test로 시작하는 메소드 실행
        Object testInstance = clazz.getDeclaredConstructor().newInstance();

        var methods = clazz.getDeclaredMethods();
        for (var method : methods) {
            if (method.getName().startsWith("test")) {
                System.out.println("Executing: " + method.getName());
                method.invoke(testInstance);
            }
        }
    }
}
