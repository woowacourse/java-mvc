package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerExecutionHandlerAdapter adapter = new HandlerExecutionHandlerAdapter();

    @Test
    void HandlerExecution을_지원한다() {
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        assertThat(adapter.supports(handlerExecution)).isTrue();
    }

    @Test
    void HandlerExecution이_아니면_지원하지_않는다() {
        assertThat(adapter.supports("test")).isFalse();
    }

    @Test
    void HandlerExecution의_결과를_그대로_반환한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);
        final ModelAndView expected = new ModelAndView(new JspView("/index.jsp"));
        when(handlerExecution.handle(request, response)).thenReturn(expected);

        final ModelAndView actual = adapter.handle(request, response, handlerExecution);

        assertThat(actual).isSameAs(expected);
    }
}
