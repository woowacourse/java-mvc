package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        //그러면 일단 메서드를 찾아야하고, 그 메서드가 test로 시작하는지 판단해야함
        for (Method method : junit3Test.getClass().getMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(junit3Test);
            }
        }
    }
}
