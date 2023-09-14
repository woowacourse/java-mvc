package reflection;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        clazz.getDeclaredMethod("test1").invoke(new Junit3Test());
        clazz.getDeclaredMethod("test2").invoke(new Junit3Test());
        clazz.getDeclaredMethod("three").invoke(new Junit3Test());
    }
}
