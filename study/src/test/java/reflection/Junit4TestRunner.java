package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        for (Method method : junit4Test.getClass().getMethods()) {
            //isAnnotationPresent의 파라미터 타입이 class네, 그러면 어노테이션의 클래스를 전달해ㅐ야함
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(junit4Test);
            }
        }
    }
}
