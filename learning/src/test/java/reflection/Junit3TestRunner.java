package reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Junit3TestRunner {
    @Test
    @DisplayName("Junit3Test에서 test로 시작하는 메소드 실행")
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        List<Method> methods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .collect(Collectors.toList());

        Junit3Test junit3Test = new Junit3Test();
        for (Method method : methods) {
            method.invoke(junit3Test);
        }
    }
}
