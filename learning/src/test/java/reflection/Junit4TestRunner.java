package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

class Junit4TestRunner {

    private Class<Junit4Test> clazz;
    private Junit4Test junit4Test;

    @BeforeEach
    void setUp() throws Exception {
        clazz = Junit4Test.class;
        Constructor<Junit4Test> constructor = clazz.getConstructor();
        junit4Test = constructor.newInstance();
    }

    @DisplayName("메서드를 모두 추출한 후 MyTest.class 어노테이션을 갖고 있는지 확인하는 방법")
    @Test
    void run1() throws Exception {
        Method[] methods = clazz.getMethods();

        List<Method> testMethods = Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .collect(Collectors.toList());

        invokeTestMethods(testMethods);
    }

    @DisplayName("MyTest.class 어노테이션을 가지고 있는 메서드들만 추출하는 방법")
    @Test
    void run2() throws Exception {
        Reflections reflections = new Reflections(clazz, new MethodAnnotationsScanner());
        Set<Method> testMethods = reflections.getMethodsAnnotatedWith(MyTest.class);

        invokeTestMethods(testMethods);
    }

    private void invokeTestMethods(Collection<Method> testMethods) throws Exception {
        for (Method testMethod : testMethods) {
            testMethod.invoke(junit4Test);
        }
    }
}
