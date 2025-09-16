package reflection;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        final var methods = clazz.getDeclaredMethods();
        for (final var method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(new Junit3Test());
            }
        }

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
    }
}
