package reflection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @DisplayName("Junit3Test에서 test로 시작하는 메소드 실행")
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Object instance = clazz.getDeclaredConstructor().newInstance();
        //public getMethod -> public 만 가져옴
        List<Method> methods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .toList();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        for (Method method : methods) {
            try {
                method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        System.setOut(System.out);
        
        String actual = outputStream.toString();

        assertAll(
                () -> assertThat(actual).contains("Running Test1"),
                () -> assertThat(actual).contains("Running Test2")
        );
    }
}
