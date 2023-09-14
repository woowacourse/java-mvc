package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<?> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Object obj = new Junit3Test();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(obj, null);
            }
        }
    }

    @Test
    void fieldTest() {
        Class<?> clazz = Junit3Test.class;
        Field[] fields = clazz.getDeclaredFields();

        assertThat(fields).isEmpty();
    }

    @Test
    void methodTest() {
        Class<?> clazz = Junit3Test.class;
        Method[] methods = clazz.getDeclaredMethods();
        List<String> methodNames = Arrays.stream(methods)
                .map(Method::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(methodNames).hasSize(3),
                () -> assertThat(methodNames)
                        .containsAll(List.of("test1", "test2", "three"))
        );
    }
}
