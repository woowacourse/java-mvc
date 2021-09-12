package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @DisplayName("Junit3Test에서 test로 시작하는 메서드 실행 - 객체 생성후 직접 호출")
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // 생성자를 통한 객체 생성
        Constructor<?> constructorOfClazz = clazz.getConstructor();
        Junit3Test junit3Test = (Junit3Test) constructorOfClazz.newInstance();

        // 메서드 호출
        junit3Test.test1();
        junit3Test.test2();
    }

    @DisplayName("Junit3Test에서 test로 시작하는 메서드 실행 - 객체 생성후 Method 객체를 통한 호출")
    @Test
    void run2() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // 생성자를 통한 객체 생성
        Constructor<?> constructorOfClazz = clazz.getConstructor();
        Junit3Test junit3Test = (Junit3Test) constructorOfClazz.newInstance();

        // 메서드 클래스 추출
        Method test1Method = clazz.getMethod("test1");
        Method test2Method = clazz.getMethod("test2");

        // 메서드 호출
        test1Method.invoke(junit3Test);
        test2Method.invoke(junit3Test);
    }
}
