package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        // getMethods()는 상속한 메서드를 포함해서, 접근제한자가 public 인 메서드들만 가져온다.
        // getDeclaredMethods()는 상속한 메서드를 제외하고, 접근제한자에 관계없이 메서드들을 가져온다.
        // getConstructor, getDeclaredConstructor도 마찬가지.

        Class<Junit3Test> clazz = Junit3Test.class;

        Constructor<Junit3Test> constructor = clazz.getConstructor();
        Junit3Test junit3Test = constructor.newInstance();

        Method[] methods = clazz.getMethods();
        List<Method> testMethods = getTestMethods(methods);

        for (Method testMethod : testMethods) {
            testMethod.invoke(junit3Test);
        }
    }

    private List<Method> getTestMethods(Method[] methods) {
        return Arrays.stream(methods)
            .filter(method -> method.getName().startsWith("test"))
            .collect(Collectors.toList());
    }
}
