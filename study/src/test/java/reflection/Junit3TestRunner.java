package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test junit3Test = clazz.getConstructor().newInstance();
        List<Method> testMethods = getDeclaredTestMethods(clazz);
        for (Method method : testMethods) {
            method.invoke(junit3Test);
        }
    }

    private List<Method> getDeclaredTestMethods(Class<Junit3Test> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.getName().startsWith("test"))
                .toList();
    }
}
