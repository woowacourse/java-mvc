package reflection;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        // constructor 흑마법
        Constructor<Junit3Test> constructor = clazz.getDeclaredConstructor();
        Junit3Test junit3Test = constructor.newInstance();

        // method 흑마법
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().contains("test"))
                .collect(Collectors.toList());

        for (Method method : methods) {
            method.invoke(junit3Test);
        }
    }
}
