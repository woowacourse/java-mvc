package reflection;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        for (var method : clazz.getDeclaredMethods()) {
            Annotation annotation = method.getAnnotation(MyTest.class);
            if (annotation != null) {
                method.invoke(new Junit4Test());
            }
        }
    }
}
