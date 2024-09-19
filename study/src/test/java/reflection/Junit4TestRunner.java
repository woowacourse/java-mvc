package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    /*

     */
    @Test
    void run() throws Exception {
        Junit4Test junit4Test = new Junit4Test();
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        // Method[] methods = clazz.getMethods(); // getMethods는 public만 반환함
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.setAccessible(true);
                method.invoke(junit4Test);
            }

        }
    }
}
