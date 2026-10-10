package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutorTest {

    private HandlerExecutor handlerExecutor;

    @BeforeEach
    void setUp() {
        final var registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerExecutor = new HandlerExecutor(registry);
    }

    @Test
    void 핸들러를_지원하는_어댑터로_실행한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        final var modelAndView = handlerExecutor.handle(request, response, createHandlerExecution());

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void 핸들러를_지원하는_어댑터가_없으면_예외가_발생한다() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        assertThatThrownBy(() -> handlerExecutor.handle(request, response, new Object()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("지원하지 않는 핸들러");
    }

    private HandlerExecution createHandlerExecution() throws NoSuchMethodException {
        final var method = TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        return new HandlerExecution(new TestController(), method);
    }
}
