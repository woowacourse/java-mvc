package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    /*
     invoke에는 해당 객체를 왜 넣어야하는가?
     */
    @Test
    void run() throws Exception {
        Junit3Test junit3Test = new Junit3Test();
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            declaredMethod.invoke(junit3Test);
        }
    }
}
