package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // 생성자를 통한 객체 생성
        Constructor<?> constructorOfClazz = clazz.getConstructor();
        Junit4Test junit4Test = (Junit4Test) constructorOfClazz.newInstance();

        // @MyTest 애노테이션을 가진 메서드 추출
        List<Method> methods = Arrays.stream(clazz.getMethods())
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .collect(Collectors.toList());

        assertThat(methods).hasSize(2);

        // 메서드 실행
        for (Method method : methods) {
            method.invoke(junit4Test);
        }
    }
}
