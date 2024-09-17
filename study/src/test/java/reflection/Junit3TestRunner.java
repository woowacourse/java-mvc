package reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = new Junit3Test();
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(junit3Test, null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });

        // TODO Junit3Test에서 test로 시작하는 메소드 실행 - 이게 최선?
        // 1. 생성자를 reflection으로 만들어서 객체 만들기
        // try catch 없앨 수 없는지 알아보기
    }
}
