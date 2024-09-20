package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance(); // 클래스로부터 생성자를 가져오고, 객체를 생성한다.
        Method[] methods = clazz.getMethods(); // 객체의 메서드를 가져온다. 상속된 메서드도 포함한다.
        List<Method> testMethods = Arrays.stream(methods)
                .filter(method -> method.getName().startsWith("test"))
                .toList();

        for (Method method : testMethods) {
            method.invoke(junit3Test); // 메서드를 실행한다. 첫번째 매개변수는 메서드를 실행할 객체를 넘겨준다.
        }
    }
}
