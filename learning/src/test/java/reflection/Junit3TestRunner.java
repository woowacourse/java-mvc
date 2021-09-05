package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @DisplayName("Junit3Test에서 test로 시작하는 메소드 실행한다.")
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Method[] methods = clazz.getMethods();
        List<Method> testMethods = Arrays.stream(methods)
            .filter(it -> it.getName().startsWith("test"))
            .collect(Collectors.toList());

        Junit3Test junit3Test = new Junit3Test();

        for (Method method : testMethods) {
            method.invoke(junit3Test);
        }
    }

}
