package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.web.annotation.Controller;

class HandlerExecutionHandlerAdapterTest {

    @Test
    @DisplayName("Handler Execution의 구현체라면 true를 반환한다.")
    void supportsWhenTrue() throws NoSuchMethodException {
        HandlerExecutionHandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

        Reflections reflections = new Reflections("samples");
        Class<?> controller = reflections.getTypesAnnotatedWith(Controller.class).iterator().next();
        Method method = controller.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);

        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
    }

    @Test
    @DisplayName("Handler Execution의 구현체가 아니라면 false를 반환한다.")
    void supportsWhenFalse() {
        HandlerExecutionHandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();
        Object object = new Object();
        assertThat(handlerAdapter.supports(object)).isFalse();
    }

}
