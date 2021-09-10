package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @DisplayName("Junit4Test에서 @MyTest 애노테이션이 있는 메소드를 실행한다.")
    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] methods = clazz.getMethods();
        List<Method> testMethods = Arrays.stream(methods)
            .filter(it -> it.isAnnotationPresent(MyTest.class))
            .collect(Collectors.toList());

        Junit4Test junit4Test = new Junit4Test();

        for (Method method : testMethods) {
            method.invoke(junit4Test);
        }
    }
}
