package reflection;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        clazz.getMethod("test1").invoke(clazz.getDeclaredConstructor().newInstance());
    }
}
