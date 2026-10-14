package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerAdapterTest {

    private final AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();

    @Test
    void supportsOnlyHandlerExecutions() {
        assertThat(adapter.supports(mock(HandlerExecution.class))).isTrue();
        assertThat(adapter.supports(mock(Controller.class))).isFalse();
    }

    @Test
    void returnsControllerModelAndView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        final var method = TestController.class.getMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final var execution = new HandlerExecution(new TestController(), method);

        final var modelAndView = adapter.handle(request, response, execution);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
