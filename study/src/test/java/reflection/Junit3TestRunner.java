package reflection;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        // 컴파일한 클래스를 런타임 때 활용하니까 리플렉션으로 볼 수 있는 듯?
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                // 인스턴스 메서드를 실행시키고 싶다면, 해당 메서드를 포함하는 객체를 넘겨줘야 하는 것 같다.
                // 여기선 Junit3Test 객체 내의 인스턴스 메서드를 실행시키고 싶으므로, Junit3Test 객체를 넘긴다.
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }
    }
}
