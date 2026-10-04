package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        final Constructor<Junit4Test> constructor = clazz.getConstructor();
        final Junit4Test node = constructor.newInstance();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(node);
            }
        }
    }


}
