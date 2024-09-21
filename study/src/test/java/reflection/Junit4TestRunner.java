package reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    @DisplayName("Mytest라는 어노테이션이 붙은 메서드만 실행시킨다.")
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(new Junit4Test());
            }
        }
    }
}
