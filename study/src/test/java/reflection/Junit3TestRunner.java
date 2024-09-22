package reflection;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test testObject = Mockito.mock(Junit3Test.class);

        Method[] methods = clazz.getDeclaredMethods(); // 상속한 메서드 제외, 접근 지정자 상관없이 모든 메서드를 가져옴
//        Method[] methods = clazz.getMethods(); // 상속한 메서드 제외, 접근 제어자가 public인 메서드만 가져옴

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(testObject); //invoke : method 호출
            }
        }

        assertAll(
                () -> Mockito.verify(testObject, times(1)).test1(),
                () -> Mockito.verify(testObject, times(1)).test2(),
                () -> Mockito.verify(testObject, never()).three()
        );
    }
}
