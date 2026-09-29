package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Method[] junit3TestMethods = clazz.getMethods();
        Junit3Test instance = clazz.getDeclaredConstructor().newInstance();

        List<Method> testMethods = Arrays.stream(junit3TestMethods)
                .filter(method -> method.getName().startsWith("test"))
                .toList();

        for(Method testMethod : testMethods){
            testMethod.invoke(instance);
        }
    }
}
