package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    /**
     * method.invoke(new Junit3Test());
     * 코드가 간단하고 명확하나 Junit3Test 클래스명이 바뀌면 수정이 필요하다.
     *
     * method.invoke(clazz.newInstance()); // deprecated
     * newInstance() 메서드는 nullary (매개변수가 없는) 생성자를 호출하는데,
     * 이 과정에서 발생할 수 있는 예외를 제대로 처리하지 못한다.
     * 특히, 생성자에서 발생하는 체크된 예외(checked exception)를 호출한 코드에서 직접 처리할 수 없다.
     *
     * method.invoke(clazz.getDeclaredConstructor().newInstance());
     * 이 방법은 생성자를 명시적으로 조회하고,
     * 생성자에서 발생할 수 있는 예외를 적절하게 포장하여 InvocationTargetException 형태로 전달한다.
     * 이로 인해 호출자는 예외를 더 잘 처리할 수 있다.
     */

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;

        System.setOut(printStream);

        try {
            // TODO Junit3Test에서 test로 시작하는 메소드 실행
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.getName().contains("test")) {
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
