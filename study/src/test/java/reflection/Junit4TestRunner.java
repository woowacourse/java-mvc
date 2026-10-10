package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Method[] junit4TestMethods = clazz.getMethods();
        Junit4Test instance = clazz.getDeclaredConstructor().newInstance();

        List<Method> myTestMethods = Arrays.stream(junit4TestMethods)
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .toList();

        for(Method myTestMethod : myTestMethods){
            myTestMethod.invoke(instance);
        }
    }
}
