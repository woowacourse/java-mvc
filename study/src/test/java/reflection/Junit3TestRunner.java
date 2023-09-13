package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;
        final Junit3Test junit3Test = new Junit3Test();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        final List<Method> test = Arrays.stream(clazz.getMethods())
            .filter(method -> method.getName().startsWith("test"))
            .collect(Collectors.toList());

        for (final Method method : test) {
            method.invoke(junit3Test);
        }
    }
}
