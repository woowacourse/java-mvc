package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    @DisplayName("Class 클래스를 통해 인스턴스의 메서드를 호출할 수 있다.")
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // 클래스를 실행할 인스턴스를 선언해야 한다.
        Junit3Test target = new Junit3Test();

        // 실행할 메서드를 가져온다. (getXXX는 public 만 가져온다)
        Method method = clazz.getMethod("test1");
        Method method1 = clazz.getMethod("test2");
        Method method2 = clazz.getMethod("three");

        // target의 인스턴스에서 해당 메서드를 실행한다.
        method.invoke(target);
        method1.invoke(target);
        method2.invoke(target);
    }
}
