package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        var methods = clazz.getDeclaredMethods();
        var instance = new Junit3Test();
        for(var method : methods){
            if(isStartsWithTest(method)) {
                method.invoke(instance);
            }
        }
    }

    private static boolean isStartsWithTest(Method method) {
        return method.getName().startsWith("test");
    }
}
