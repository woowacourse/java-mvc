package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // getDeclaredConstructor() : 기본 생성자의 Constructor 객체를 가져옴 ( 인자를 주면 특정 생성자 선택 )
        // newInstance() : 그 생성자를 실제로 호출해 객체를 만듦 ( new Junit3Test() 와 결과는 같지만 new 를 쓰지 않음 )
        Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();

        // getDeclaredMethods() : 구현 되어있는 모든 메서드 불러옴 ( private, 상관없이 전부 가져옴 )
        // getName() : 메서드 이름을 String 으로 꺼냄 -> startsWith("test") 로 걸러서 three() 는 제외됨
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> runTest(method, junit3Test));
    }

    // invoke() : Method 가 가리키는 메서드를 실제로 실행 ( 첫 번째 인자 = 호출 대상 객체, 두 번째부터 = 메서드에 넘길 인자 )
    //            반환값은 Object, void 메서드면 null
    private void runTest(final Method method, final Object instance) {
        try {
            method.invoke(instance);
        } catch (Exception e) {
            throw new IllegalStateException(method.getName() + " 실행에 실패했습니다.", e);
        }
    }
}
