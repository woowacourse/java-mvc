package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        List<String> methods = Arrays.stream(clazz.getMethods())
                                  .map(Method::getName)
                                  .filter(name -> name.startsWith("test"))
                                  .collect(Collectors.toList());

        for (String method : methods) {
            Junit3Test instance = clazz.getDeclaredConstructor().newInstance();
            clazz.getMethod(method).invoke(instance);
        }
    }
}
