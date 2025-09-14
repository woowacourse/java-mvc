package reflection;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test test = new Junit3Test();
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(test);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
