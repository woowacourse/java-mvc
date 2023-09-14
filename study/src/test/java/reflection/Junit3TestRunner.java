package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        // 클래스 가져오기
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        // 가져온 클래스의 생성자 가져오기
        Constructor<Junit3Test> declaredConstructor = clazz.getDeclaredConstructor();

        // 가져온 생성자로 인스턴스 생성하기
        Junit3Test junit3Test = declaredConstructor.newInstance();

        // 클래스의 메소드들 가져오기
        Method[] test = clazz.getDeclaredMethods();
        for (Method testMethod : test) {
            // test로 시작하는 메소드들 호출
            if (testMethod.getName().startsWith("test")) {
                testMethod.invoke(junit3Test);
            }
        }
    }
}
