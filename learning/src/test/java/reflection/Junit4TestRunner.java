package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        List<Method> runMethods = Arrays.stream(clazz.getMethods())
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .collect(Collectors.toList());

        Junit4Test junit4Test = new Junit4Test();
        
        for (Method method : runMethods) {
            method.invoke(junit4Test);
        }
    }
}
