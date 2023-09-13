package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        final Junit4Test junit4Test = clazz.newInstance();
        final Method[] declaredMethods = clazz.getDeclaredMethods();

        for (Method m: declaredMethods) {
            if(m.isAnnotationPresent(MyTest.class)){
                m.invoke(junit4Test);
            }
        }
    }
}
