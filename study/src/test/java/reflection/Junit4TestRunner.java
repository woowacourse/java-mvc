package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;

        System.setOut(printStream);


        try {
            // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(MyTest.class)) {
                    method.invoke(clazz.getDeclaredConstructor().newInstance());
                }
            }
        } finally {
            System.setOut(originalOut);
        }

        String resultString = outputStream.toString();

        assertThat(resultString).isEqualTo("""
                Running Test1
                Running Test2
                """);
    }
}
