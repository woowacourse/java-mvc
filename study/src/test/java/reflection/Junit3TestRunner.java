package reflection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Arrays.stream(clazz.getMethods())
                .filter(c -> c.getName().startsWith("test"))
                .forEach(c -> {
                    try {
                        c.invoke(clazz.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
