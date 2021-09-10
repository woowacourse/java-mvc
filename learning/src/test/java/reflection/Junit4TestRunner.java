package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getConstructor().newInstance();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Class<MyTest> myTestClazz = MyTest.class;
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> Objects.nonNull(method.getAnnotation(myTestClazz)))
                .collect(Collectors.toList());

        for (Method m : methods) {
            m.invoke(junit4Test);
        }
    }
}
