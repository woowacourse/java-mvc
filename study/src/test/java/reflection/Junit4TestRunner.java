package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    @DisplayName("Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행 성공")
    void run() throws Exception {
        // given
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // When
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test testObj = clazz.getConstructor()
                .newInstance();
        Class<MyTest> annotationClass = MyTest.class;

        List<Method> methods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .toList();
        for (Method method : methods) {
            method.invoke(testObj);
        }

        // Then
        assertThat(outContent.toString())
                .contains("Running Test1", "Running Test2")
                .doesNotContain("Running Test3");

        System.setOut(originalOut);
    }
}
