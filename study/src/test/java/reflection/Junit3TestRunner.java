package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        final Junit3Test junit3Test = clazz.getConstructor().newInstance();
/*        final Method test1 = clazz.getMethod("test1");
        final Method test2 = clazz.getMethod("test2");*/
        final Method[] methods = clazz.getMethods(); // 상속한 Object 클래스의 메서드까지 다 가져옴

        for (Method method : methods) {
            System.out.println("method.getName() = " + method.getName());
            if(method.getName().startsWith("test")) {
                method.invoke(junit3Test);
            }
        }
    }
}
