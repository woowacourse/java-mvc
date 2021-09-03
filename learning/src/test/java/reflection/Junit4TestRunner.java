package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    @DisplayName("Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행")
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getConstructor().newInstance();
        List<Method> methods = Arrays.stream(clazz.getMethods())
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .collect(Collectors.toList());
        for (Method method : methods) {
            method.invoke(junit4Test);
        }
    }
}
