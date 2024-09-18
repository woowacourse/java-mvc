package reflection;

import static java.util.Arrays.stream;

import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        List<Method> methods = stream(clazz.getDeclaredMethods())
                .filter(it -> stream(it.getDeclaredAnnotations()).anyMatch(an -> an instanceof MyTest))
                .toList();

        for (Method it : methods) {
            it.invoke(junit4Test);
        }
    }

    @Test
    @DisplayName("isAnnotationPresent")
    void isAnnotationPresent() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        List<Method> methods = stream(clazz.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(MyTest.class))
                .toList();

        for (Method it : methods) {
            it.invoke(junit4Test);
        }
    }
}
