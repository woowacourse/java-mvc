package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AnnotationHandlerAdapterTest {

    private final AnnotationHandlerAdapter adapter =
            new AnnotationHandlerAdapter();

    @Test
    void supports() {
        final HandlerExecution execution = mock(HandlerExecution.class);

        assertThat(adapter.supports(execution)).isTrue();
        assertThat(adapter.supports(new Object())).isFalse();
    }

    @Test
    void handle() throws Exception {
        final HandlerExecution execution = mock(HandlerExecution.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final ModelAndView expected = new ModelAndView(
                new JspView("/test.jsp")
        );

        when(execution.handle(request, response)).thenReturn(expected);

        final ModelAndView actual = adapter.handle(
                execution, request, response
        );

        assertThat(actual).isSameAs(expected);
        verify(execution).handle(request, response);
    }
}
