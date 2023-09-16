package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        final Junit4Test junit4Test = clazz.getConstructor().newInstance();
        final Method[] methods = clazz.getDeclaredMethods(); // 상속한 메서드 제외

        for (Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(junit4Test);
            }
        }
    }
}
