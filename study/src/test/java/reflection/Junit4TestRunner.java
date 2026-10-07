package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = new Junit4Test();

        Method[] methods = clazz.getMethods();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        for (Method method : methods) {
            MyTest mytest = method.getAnnotation(MyTest.class);
            if (mytest != null) {
                method.invoke(junit4Test);
            }
        }
    }
}
