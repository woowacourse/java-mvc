package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

    @Test
    void 핸들러_실행_객체만_지원한다() throws Exception {
        final var handlerExecution = createHandlerExecution();
        final Controller controller = (request, response) -> "/index.jsp";

        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
        assertThat(handlerAdapter.supports(controller)).isFalse();
        assertThat(handlerAdapter.supports(null)).isFalse();
    }

    @Test
    void 어노테이션_컨트롤러_메서드를_실행하고_모델을_유지한다() throws Exception {
        final var handlerExecution = createHandlerExecution();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        final var modelAndView = handlerAdapter.handle(request, response, handlerExecution);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        assertThat(modelAndView.getView()).isNotNull();
    }

    private HandlerExecution createHandlerExecution() throws NoSuchMethodException {
        final var controller = new TestController();
        final var method = TestController.class.getMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class);
        return new HandlerExecution(controller, method);
    }
}
