package reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    @DisplayName("test라는 이름이 붙은 메서드만 실행시킨다.")
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(new Junit3Test());
            }
        }
    }
}
