package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    /**
     * clazz.getMethods() : public 메소드만 반환
     * clazz.getDeclaredMethods() : 모든 메소드 반환
     */
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test junit3Test = clazz.getConstructor().newInstance();
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
            try {
                method.setAccessible(true);
                method.invoke(junit3Test);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
