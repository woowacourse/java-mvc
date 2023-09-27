package webmvc.org.springframework.web.servlet.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

class AnnotationHandlerAdapterTest {

    @DisplayName("Handler 가 HandlerExecution 이라면 true가 반환된다.")
    @Test
    void supports() {
        // given
        final AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final HandlerExecution handler = mock(HandlerExecution.class);

        // when
        final boolean supports = handlerAdapter.supports(handler);

        // then
        assertThat(supports).isTrue();
    }

    @DisplayName("handle 호출 시 HandlerExecution 의 handle 이 호출된다.")
    @Test
    void handle() throws Exception {
        // given
        final HandlerExecution handler = mock(HandlerExecution.class);
        final AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(handler.handle(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(new ModelAndView(new JspView("test")));

        // when
        final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        // then
        assertThat(modelAndView.getView()).isNotNull();
        verify(handler, times(1)).handle(request, response);
    }
}
