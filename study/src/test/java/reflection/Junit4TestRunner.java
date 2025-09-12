package reflection;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Object testInstance = clazz.getDeclaredConstructor().newInstance();

        var methods = clazz.getDeclaredMethods();
        for (var method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                System.out.println("Executing: " + method.getName());
                method.invoke(testInstance);
            }
        }
    }
}
