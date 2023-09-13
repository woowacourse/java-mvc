package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        final Junit3Test junit3Test = clazz.getConstructor().newInstance();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        final Method[] declaredMethods = clazz.getDeclaredMethods(); // getMethods()와의 차이 getDeclaredMethods()는 현재 자신의 클래스에 있는 메소드만 불러온다.
        final List<Method> testMethods = Arrays.stream(declaredMethods)
            .filter(method -> method.getName().startsWith("test"))
            .collect(Collectors.toUnmodifiableList());

        for (Method testMethod : testMethods) {
            testMethod.invoke(junit3Test);
        }
    }
}
