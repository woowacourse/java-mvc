package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Junit4Test instance = clazz.getDeclaredConstructor().newInstance();
        Optional<Method> testMethod = Arrays.stream(clazz.getDeclaredMethods())
                .filter((method) -> method.isAnnotationPresent(MyTest.class))
                .findFirst();
        if (testMethod.isPresent()) {
            testMethod.get().invoke(instance);
        }
    }
}
