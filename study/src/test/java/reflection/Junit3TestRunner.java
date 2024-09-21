package reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test spiedInstance = Mockito.spy(clazz.getDeclaredConstructor().newInstance());
        /*
        clazz.newInstance() 는 JDK 9부터 deprecated 되었다.
        이는 인자가 없는 생성자만 호출할 수 있었고, 생성자에서 던지는 예외를 그대로 전파하여
        컴파일러의 예외 검사를 우회하게 되는 문제가 있었다. (Checked Exception도 그냥 던져버리므로)
        즉, 컴파일 타임에 처리돼야 할 예외가 런타임으로 미뤄졌다.
        대안으로 제시된 getDeclaredConstructor().newInstance()는 InvocationTargetException을 던지는데,
        이는 생성자 호출 시 발생하는 예외를 런타임으로 처리하는 것이 아니라, 호출자에게 예외를 던지도록 한다.
         */

        // Junit3Test에서 test로 시작하는 메소드 실행
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(spiedInstance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });

        Mockito.verify(spiedInstance, Mockito.times(1)).test1();
        Mockito.verify(spiedInstance, Mockito.times(1)).test2();
        Mockito.verify(spiedInstance, Mockito.never()).three();
    }
}
