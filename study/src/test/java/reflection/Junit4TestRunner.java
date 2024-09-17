package reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @DisplayName("Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행")
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(testMethod-> {
                    try {
                        testMethod.invoke(instance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
