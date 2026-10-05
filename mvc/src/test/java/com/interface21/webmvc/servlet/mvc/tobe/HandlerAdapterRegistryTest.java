package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @Test
    @DisplayName("HandlerExecution을 HandlerExecutionAdapter로 핸들링한다")
    void handlesHandlerExecution() throws NoSuchMethodException {
        HandlerExecution handlerExecution = createHandlerExecution();

        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

        assertThat(handlerAdapter).isInstanceOf(HandlerExecutionAdapter.class);
    }

    @Test
    @DisplayName("아무 어댑터도 처리할 수 없는 핸들러이면 예외를 던진다")
    void throwsWhenNoAdapterCanHandle() {
        Object unsupportedHandler = new Object();

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(unsupportedHandler))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private HandlerExecution createHandlerExecution() throws NoSuchMethodException {
        TestController controller = new TestController();
        Method method = TestController.class.getDeclaredMethod(
                "findUserId",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        return new HandlerExecution(controller, method);
    }
}
