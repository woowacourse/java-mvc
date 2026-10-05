package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerExecutionHandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

    @Test
    void HandlerExecution_타입의_핸들러만_지원한다() throws Exception {
        assertThat(handlerAdapter.supports(createHandlerExecution())).isTrue();
        assertThat(handlerAdapter.supports(new Object())).isFalse();
    }

    @Test
    void HandlerExecution을_실행해_ModelAndView를_반환한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        final var modelAndView = handlerAdapter.handle(request, response, createHandlerExecution());

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    private HandlerExecution createHandlerExecution() throws NoSuchMethodException {
        final var method = TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        return new HandlerExecution(new TestController(), method);
    }
}
