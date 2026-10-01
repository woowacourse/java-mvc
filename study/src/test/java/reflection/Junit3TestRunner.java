package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test target = new Junit3Test();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        /**
        Method method1 = clazz.getMethod("test1");
        method1.invoke(target);

        Method method2 = clazz.getMethod("test2");
        method2.invoke(target);

        Method method3 = clazz.getMethod("three");
        method3.invoke(target);
         **/

        /**
        # Method[] methods = clazz.getMethods();

        getMethods()로 가져오게 된다면 상속받는 모든 메서드도 가지고 오기 때문에 Junit3Test만 실행되지 않는다.
        모든 자바 클래스는 기본적으로 Object를 상속받기 때문에 Object 내부 인수가 있는 메서드들 때문에 오류가 나게 된다.
        **/

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            // 매개변수가 없는 메서드만 필터링하여 안전하게 호출
            System.out.println("[" + method.getName() + "] 메서드 자동 호출 시작");

            // invoke(실행할_인스턴스, 전달할_매개변수...)
            method.invoke(target);
        }
    }
}
