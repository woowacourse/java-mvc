package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JUnit3TestRunner {

    private static final Logger log = LoggerFactory.getLogger(JUnit3TestRunner.class);

    /*
    getMethod() vs. getDeclaredMethod()
    getDeclaredMethod는 해당 클래스에 선언된 메소드만 찾음,
    getMethod는 Object class로부터 상속받은 메소드까지 찾음

    invoke(Obj, args)로 실행하게 되면 Obj.method(args)와 같다.
    메서드가 static인 경우에는 Obj가 null이어도 된다.

    obj로부터 메서드가 불리게 되는데, obj가 상태를 가지는 경우 굉장히 불안해질 수 있음.
    매번 객체를 만들어내는 것이 안전할 때도 있지만, 그만큼의 객체 생성 비용이 든다.
    객체는 clazz.getConstructor().newInstance()로 생성할 수 있다.
    리플렉션은 정말 편하지만... 흑마법이다. 조심해서 사용하자.
     */
    @Test
    void run() throws Exception {
        Class<JUnit3Test> clazz = JUnit3Test.class;
        Method[] methods = clazz.getMethods();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        log.info("methods: {}\n", String.join("\n", Arrays.toString(methods)));
        log.info("declaredMethods: {}", String.join("\n", Arrays.toString(declaredMethods)));

        int invokedMethodCount = 0;
        JUnit3Test test = new JUnit3Test();
        for (Method method : declaredMethods) {
            if (method.getName().startsWith("test")) {
                method.invoke(test);
                invokedMethodCount++;
            }
        }
        assertThat(invokedMethodCount).isEqualTo(2);
    }
}
