package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import samples.TestReflections;

class HandlerMethodTest {

    @Test
    void invoke() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestReflections executionObject = new TestReflections();
        Method method = TestReflections.class.getMethod("testMethod");

        assertThat(new HandlerMethod(executionObject, method).invoke()).isEqualTo("test.jsp");
    }
}
