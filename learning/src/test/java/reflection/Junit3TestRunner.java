package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();

        Method[] methods = clazz.getMethods();

        List<Method> startsWithTest = Arrays.stream(methods)
                .filter(method -> method.getName().startsWith("test"))
                .collect(Collectors.toList());

        for(Method m : startsWithTest) {
            m.invoke(junit3Test);
        }
    }
}
