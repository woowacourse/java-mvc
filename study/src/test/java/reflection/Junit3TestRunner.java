package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Method[] methods = clazz.getDeclaredMethods();
        Junit3Test junit3Test = clazz.getConstructor().newInstance();
        Arrays.stream(methods)
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(junit3Test);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
