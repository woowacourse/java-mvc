package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Method[] methods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .toArray(Method[]::new);

        System.out.println(Arrays.toString(methods));

        for (Method method : methods) {
            method.invoke(clazz.getDeclaredConstructor().newInstance());
            System.out.println(method);
        }

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
    }
}
