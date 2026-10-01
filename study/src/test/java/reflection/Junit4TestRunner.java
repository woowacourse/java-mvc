package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        // Junit4Test에서 @MyTest 애노테이션이 있는 메서드 실행
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4TestInstance = new Junit4Test();
        TargetAnnotation testAnnotation = new TargetAnnotation("MyTest");

        Method[] methods = clazz.getDeclaredMethods();
        Arrays.sort(methods, (left, right) -> left.getName().compareTo(right.getName()));

        for (Method method : methods) {
            boolean isIncluded = testAnnotation.included(method)
                    && method.getParameterCount() == 0
                    && method.getReturnType() == void.class;

            if (isIncluded) {
                method.invoke(junit4TestInstance);
            }
        }
    }
}
