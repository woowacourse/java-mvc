package reflection;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test junit3Test = new Junit3Test();
        junit3Test.test1();
        junit3Test.test2();
    }
}
