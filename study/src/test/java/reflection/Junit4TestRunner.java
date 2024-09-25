package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Constructor<Junit4Test> constructor = clazz.getConstructor();
        Junit4Test junit4Test = constructor.newInstance();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(junit4Test);
            }
        }
    }
}
