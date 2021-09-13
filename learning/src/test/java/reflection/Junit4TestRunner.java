package reflection;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Class<MyTest> myTestClass = MyTest.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행


        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(myTestClass)) {
                Junit4Test instance = clazz.getDeclaredConstructor().newInstance();
                method.invoke(instance);
            }
        }
    }
}
