package reflection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Arrays.stream(clazz.getMethods())
                .filter(c -> c.isAnnotationPresent(MyTest.class))
                .forEach(c -> {
                    try {
                        c.invoke(clazz.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
