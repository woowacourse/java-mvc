package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        //given
        Class<Junit3Test> clazz = Junit3Test.class;
        //when
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        final Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();
        //then
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().startsWith("test")) {
                declaredMethod.invoke(junit3Test);
            }
        }
    }
}
