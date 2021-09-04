package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Object obj = clazz.getConstructor().newInstance();

        Method test1 = clazz.getMethod("test1");
        Method test2 = clazz.getMethod("test2");
        Method three = clazz.getMethod("three");

        test1.invoke(obj);
        test2.invoke(obj);
        three.invoke(obj);
    }
}
