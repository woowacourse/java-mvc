package reflection;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Junit4Test junit4Test = new Junit4Test();
        Arrays.stream(junit4Test.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> {
                    try {
                        method.invoke(junit4Test);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
