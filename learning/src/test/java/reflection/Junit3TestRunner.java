package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = clazz.getConstructor().newInstance();

        // Junit3Test에서 test로 시작하는 메소드 실행
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().startsWith("test"))
                .collect(Collectors.toList());

        /*
         * 지정된 매개 변수를 사용하여 지정된 개체에서 이 메서드 개체로 표시되는 기본 메서드를 호출합니다.
         * 개별 매개 변수는 원시 형식 매개 변수와 일치하도록 자동으로 래핑 해제되며,
         * 필요에 따라 기본 매개 변수와 참조 매개 변수 모두 메서드 호출 변환의 대상이 됩니다.
         */

        for (Method method : methods) {
            method.invoke(junit3Test);
        }
    }
}
