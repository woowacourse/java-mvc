package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    @DisplayName("Junit3Test에서 test로 시작하는 메소드 실행")
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = clazz.getConstructor().newInstance();

        Method[] methods = clazz.getDeclaredMethods();
        List<Method> methodsStartsWithTest = Arrays.stream(methods)
            .filter(m -> m.getName().startsWith("test"))
            .collect(Collectors.toList());
        for (Method method : methodsStartsWithTest) {
            method.invoke(junit3Test);
        }
    }
}
