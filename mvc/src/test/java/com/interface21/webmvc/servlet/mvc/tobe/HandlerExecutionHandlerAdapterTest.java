package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionHandlerAdapterTest {

    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new HandlerExecutionHandlerAdapter();
    }

    @Test
    void HandlerExecution을_지원한다() throws Exception {
        final var handlerExecution = createHandlerExecution();

        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
    }

    @Test
    void HandlerExecution이_아니면_지원하지_않는다() {
        final var notHandlerExecution = new Object();

        assertThat(handlerAdapter.supports(notHandlerExecution)).isFalse();
    }

    @Test
    void HandlerExecution을_실행하고_ModelAndView를_그대로_반환한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        final var modelAndView = handlerAdapter.handle(request, response, createHandlerExecution());

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    private HandlerExecution createHandlerExecution() throws NoSuchMethodException {
        final var method = TestController.class.getDeclaredMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class);
        return new HandlerExecution(new TestController(), method);
    }
}
