package com.interface21.webmvc.servlet.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    private final AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();

    @Test
    void supportsHandlerExecution() {
        assertThat(adapter.support(mock(HandlerExecution.class))).isTrue();
        assertThat(adapter.support(new Object())).isFalse();
    }

    @Test
    void delegatesToHandlerExecution() throws Exception {
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ModelAndView expected = mock(ModelAndView.class);
        when(handlerExecution.handle(request, response)).thenReturn(expected);

        final ModelAndView actual = adapter.handle(request, response, handlerExecution);

        assertThat(actual).isSameAs(expected);
        verify(handlerExecution).handle(request, response);
    }
}
