package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        ByteArrayOutputStream systemOutputStream = getSystemOutputStream();

        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        List<Method> myTestAnnotationedMethods = Arrays.stream(clazz.getDeclaredMethods()) // 상속된 메서드를 제외하고
                .filter(method -> method.isAnnotationPresent(MyTest.class)) // 메서드에 어노테이션이 달린 것을 찾는다.
                .toList();

        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();

        for (Method myTestAnnotationedMethod : myTestAnnotationedMethods) {
            myTestAnnotationedMethod.invoke(junit4Test);
        }

        String printedString = systemOutputStream.toString();
        assertThat(printedString)
                .contains("Running Test1", "Running Test2");
    }

    private ByteArrayOutputStream getSystemOutputStream() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setOut(printStream);
        return byteArrayOutputStream;
    }
}
