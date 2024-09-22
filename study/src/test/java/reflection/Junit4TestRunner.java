package reflection;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test testObject = Mockito.mock(Junit4Test.class);

        Method[] declaredMethods = clazz.getDeclaredMethods();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        // method.getParameterAnnotations() : 인수에 부여된 어노테이션 정보
        // method.getDeclaredAnnotations() : 메서드 위에 선언된 모든 어노테이션 -> [메서드별][어노테이션 파라미터]
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(testObject);
            }
        }

        assertAll(
                ()-> Mockito.verify(testObject, times(1)).one(),
                ()-> Mockito.verify(testObject, times(1)).two(),
                ()-> Mockito.verify(testObject, never()).testThree()
        );
    }
}
