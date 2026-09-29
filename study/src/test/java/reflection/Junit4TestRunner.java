package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    @DisplayName("Class 클래스로 특정 애노테이션이 포함된 메서드를 조회할 수 있다.")
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test target = new Junit4Test();

        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(target);
            }
        }

    }
}
