package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO getMethods()와 getDeclaredMethods() 차이는?
        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("test")) {
                // TODO invoke에 전달해야 되는 건 뭘까?
                // object is not an instance of declaring class
                // https://stackoverflow.com/questions/13336057/java-reflection-object-is-not-an-instance-of-declaring-class
                method.invoke(new Junit3Test());
            }
        }

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
    }
}
