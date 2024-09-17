package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @DisplayName("Junit3Test에서 test로 시작하는 메소드 실행")
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Object instance = clazz.getDeclaredConstructor().newInstance();
        //public getMethod -> public 만 가져옴
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(testMethod-> {
                    try {
                        testMethod.invoke(instance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
