package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    Class<Junit3Test> clazz = Junit3Test.class;

    @Test
    void run() throws Exception {
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test junit3Test = clazz.getConstructor().newInstance();
        List<Method> testMethods = getDeclaredTestMethods(clazz);
        assertThat(testMethods).hasSize(2);
        for (Method method : testMethods) {
            method.invoke(junit3Test);
        }
    }

    private List<Method> getDeclaredTestMethods(Class<Junit3Test> clazz) {
        Method[] methods = clazz.getMethods();
        return Arrays.stream(methods)
                .filter(method -> method.getName().startsWith("test"))
                .toList();
    }

    @DisplayName("getMethods: 상속 메서드를 포함한 모든 public 메서드를 반환한다.")
    @Test
    void getMethods() {
        // given & when
        Method[] methods = clazz.getMethods();

        // then
        List<String> actual = Arrays.stream(methods)
                .map(Method::getName)
                .toList();
        assertThat(actual).containsExactly(
                "test1",
                "test2",
                "equals",
                "toString",
                "hashCode",
                "getClass",
                "notify",
                "notifyAll",
                "wait",
                "wait",
                "wait");
    }

    @DisplayName("getDeclaredMethods: 상속 메서드를 제외하고 직접 선언된 모든 메서드를 반환한다.")
    @Test
    void getDeclaredMethods() {
        // given & when
        Method[] methods = clazz.getDeclaredMethods();

        // then
        List<String> actual = Arrays.stream(methods)
                .map(Method::getName)
                .toList();
        assertThat(actual).containsExactly(
                "test1",
                "test2",
                "privateTest3");
    }
}
