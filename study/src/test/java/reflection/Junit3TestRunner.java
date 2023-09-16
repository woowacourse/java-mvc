package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Object junit3Test = clazz.getConstructor().newInstance();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for(Method method : declaredMethods) {
            method.invoke(junit3Test);
        }
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
    }
}
