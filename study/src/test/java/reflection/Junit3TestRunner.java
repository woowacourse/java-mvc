package reflection;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // Junit3Test에서 test로 시작하는 메소드 실행
        final var instance = clazz.getDeclaredConstructor().newInstance();
        for (var method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(instance);
            }
        }
    }
}
