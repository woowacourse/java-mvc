package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleControllerHandlerAdapterTest {
    HandlerAdapter handlerAdapter = new SimpleControllerHandlerAdapter();

    @Test
    void supportsController() {
        // given
        Controller handler = new ForwardController("/");

        // when & then
        assertThat(handlerAdapter.supports(handler)).isTrue();
    }

    @Test
    void nonSupportsController() {
        // given
        HandlerExecution handler = new HandlerExecution(null, null);

        // when & then
        assertThat(handlerAdapter.supports(handler)).isFalse();
    }

    @Test
    void successHandling() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        Controller handler = new ForwardController("/index.jsp");

        // when
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        // then
        assertThat(modelAndView.getView()).usingRecursiveComparison()
                .isEqualTo(new JspView("/index.jsp"));
    }
}
