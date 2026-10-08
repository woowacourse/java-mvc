package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionAdapterTest {

    private final HandlerExecutionAdapter handlerAdapter = new HandlerExecutionAdapter();

    @Test
    void givenHandlerExecution_whenChecksSupport_thenReturnsTrue() throws Exception {
        final var handlerExecution = createHandlerExecution();

        final var result = handlerAdapter.supports(handlerExecution);

        assertThat(result).isTrue();
    }

    @Test
    void givenUnsupportedHandler_whenChecksSupport_thenReturnsFalse() {
        final var handler = new Object();

        final var result = handlerAdapter.supports(handler);

        assertThat(result).isFalse();
    }

    @Test
    void givenHandlerExecution_whenHandles_thenReturnsModelAndView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        final var handlerExecution = createHandlerExecution();

        final var modelAndView = handlerAdapter.handle(request, response, handlerExecution);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    private HandlerExecution createHandlerExecution() throws Exception {
        final var controller = new TestController();
        final var method = TestController.class.getDeclaredMethod(
                "findUserId",
                HttpServletRequest.class,
                HttpServletResponse.class
        );

        return new HandlerExecution(controller, method);
    }
}
