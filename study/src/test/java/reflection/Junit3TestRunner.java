package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Method test1 = clazz.getMethod("test1");
        Method test2 = clazz.getMethod("test2");
        Method three = clazz.getMethod("three");

        test1.invoke(new Junit3Test());
        test2.invoke(new Junit3Test());
        three.invoke(new Junit3Test());
    }
}
