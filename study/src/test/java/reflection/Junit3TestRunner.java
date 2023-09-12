package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        // 객체 생성 가능
        final Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();

        // 1. 객체에서 바로 메서드 호출
        junit3Test.test1();
        junit3Test.test2();

        // 2. getDeclaredMethod로 메서드 찾아서 호출
        final Method test1 = clazz.getDeclaredMethod("test1");
        final Method test2 = clazz.getDeclaredMethod("test2");
        test1.invoke(clazz.cast(junit3Test));
        test2.invoke(clazz.cast(junit3Test));

        // 3. getDeclaredMethods로 메서드 배열 가져온 후 for문 돌면서 호출
        final Method[] methods = clazz.getDeclaredMethods();
        for (final Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(clazz.cast(junit3Test));
            }
        }
    }
}
