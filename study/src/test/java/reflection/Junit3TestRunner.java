package reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();

        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            String methodName = declaredMethod.getName();
            if (methodName.startsWith("test")) {
                declaredMethod.invoke(junit3Test);
            }
        }
    }

    @Test
    @DisplayName("declared constructor는 주어진 클래스 파라미터에 해당하는 생성자를 반환한다.")
    void declaredConstructor() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Constructor<Junit3Test> constructor = clazz.getConstructor(String.class, String.class);
        Junit3Test junit3Test = constructor.newInstance("hello", "world");
    }
}
